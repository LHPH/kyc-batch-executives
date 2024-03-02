package com.kyc.batch.executive.management.validator;

import com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import com.kyc.batch.executive.management.repository.KycExecutiveRepository;

import com.kyc.core.enums.MessageType;
import com.kyc.core.exception.KycBatchException;
import com.kyc.core.model.MessageData;
import com.kyc.core.properties.KycMessages;
import com.kyc.core.validation.engine.ValidationRuleEngine;
import com.kyc.core.validation.model.InputData;
import com.kyc.core.validation.model.ResultValidation;
import com.kyc.core.validation.model.RuleValidation;
import com.kyc.core.validation.rules.ExpectPatternRuleValidation;
import com.kyc.core.validation.rules.NonNullRuleValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kyc.batch.executive.management.constants.KycBatchExecutiveConstants.ADM_EXECUTIVE_STEP;
import static com.kyc.core.constants.RegExConstants.REGEX_NAME;
import static com.kyc.core.constants.RegExConstants.REGEX_NUMERICS;
import static com.kyc.core.constants.RegExConstants.REGEX_PHONES;
import static com.kyc.core.util.GeneralUtil.getType;
import static com.kyc.core.util.GeneralUtil.setNullIfEquals;

public class ExecutiveRecordValidator implements Validator<ExecutiveRawData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutiveRecordValidator.class);

    private final KycExecutiveRepository kycExecutiveRepository;
    private final ValidationRuleEngine validationRuleEngine;
    private final KycMessages kycMessages;

    private final Map<String,RuleValidation<?>> rules;
    
    private static final String FIRST_NAME = "firstName";
    private static final String SECOND_NAME = "secondName";
    private static final String LAST_NAME = "lastName";
    private static final String LAST_SECOND_NAME = "lastSecondName";
    private static final String RFC = "rfc";
    private static final String PHONE = "phone";
    private static final String EXTENSION = "extension";
    private static final String EMAIL = "email";
    private static final String ID_BRANCH = "idBranch";

    public ExecutiveRecordValidator(KycExecutiveRepository kycExecutiveRepository,
                                    ValidationRuleEngine validationRuleEngine,
                                    KycMessages kycMessages){

        this.kycExecutiveRepository = kycExecutiveRepository;
        this.validationRuleEngine = validationRuleEngine;
        this.kycMessages = kycMessages;

        rules = new HashMap<>();

        rules.put(FIRST_NAME,new ExpectPatternRuleValidation(REGEX_NAME,true,"Bad First Name"));
        rules.put(SECOND_NAME,new ExpectPatternRuleValidation(REGEX_NAME,false,"Bad Second Name"));
        rules.put(LAST_NAME,new ExpectPatternRuleValidation(REGEX_NAME,true,"Bad Last Name"));
        rules.put(LAST_SECOND_NAME,new ExpectPatternRuleValidation(REGEX_NAME,false,"Bad Second Last Name"));
        rules.put(RFC,new ExpectPatternRuleValidation("^[A-Z0-9]{10,13}$",true,"Bad RFC"));
        rules.put(PHONE,new ExpectPatternRuleValidation(REGEX_PHONES,true,"Bad Phone"));
        rules.put(EXTENSION,new ExpectPatternRuleValidation(REGEX_NUMERICS,false,"Bad Extension"));
        rules.put(EMAIL,new ExpectPatternRuleValidation("^[\\w\\.\\d@]+$",true,"Bad Email"));
        rules.put(ID_BRANCH,new NonNullRuleValidation(true,"Null Data"));

    }

    @Override
    public void validate(ExecutiveRawData value) throws ValidationException {

        BatchExecutiveProcessEnum operation = BatchExecutiveProcessEnum.getInstance(value.getOperation());

        Long executiveNumber = value.getExecutiveNumber();
        List<ResultValidation> results = new ArrayList<>();
        switch(operation){
            case REGISTRATION:
                addExecutiveValidations(results,value);
                break;
            case MODIFICATION:
                addExecutiveNumberValidation(results,executiveNumber);
                addExecutiveValidations(results,value);
                break;
            case ENABLING:
            case DISABLING:
                addExecutiveNumberValidation(results,executiveNumber);
                break;
            default:
                MessageData messageData = kycMessages.getMessage("001");
                messageData.setMessage("Invalid Value: "+operation);
                throw KycBatchException.builderBatchException()
                        .batchStepName(ADM_EXECUTIVE_STEP)
                        .inputData(value)
                        .errorData(messageData)
                        .exitStatus(ExitStatus.FAILED)
                        .build();
        }

        Optional<ResultValidation> opResult = validationRuleEngine.getFirstFailure(results);
        if(opResult.isPresent()){

            ResultValidation resultValidation = opResult.get();
            String message = "Field: "+resultValidation.getField()+", error: "+resultValidation.getError();
            MessageData messageData = kycMessages.getMessage("001");
            messageData.setMessage(message);

            throw KycBatchException.builderBatchException()
                    .batchStepName(ADM_EXECUTIVE_STEP)
                    .inputData(value)
                    .errorData(messageData)
                    .exitStatus(ExitStatus.FAILED)
                    .build();
        }
    }

    private void addExecutiveValidations(List<ResultValidation> results, ExecutiveRawData value){

        ExpectPatternRuleValidation ruleFirstName = getType(rules.get(FIRST_NAME),ExpectPatternRuleValidation.class);
        InputData<String> inputFirstName = new InputData<>(FIRST_NAME,value.getFirstName());
        results.add(ruleFirstName.isValid(inputFirstName));

        ExpectPatternRuleValidation ruleSecondName = getType(rules.get(SECOND_NAME),ExpectPatternRuleValidation.class);
        InputData<String> inputSecondName = new InputData<>(SECOND_NAME,setNullIfEquals(value.getSecondName(),""));
        results.add(ruleSecondName.isValid(inputSecondName));

        ExpectPatternRuleValidation ruleLastName = getType(rules.get(LAST_NAME),ExpectPatternRuleValidation.class);
        InputData<String> inputLastName = new InputData<>(LAST_NAME,value.getLastName());
        results.add(ruleLastName.isValid(inputLastName));

        ExpectPatternRuleValidation ruleLastSecondName = getType(rules.get(LAST_SECOND_NAME),ExpectPatternRuleValidation.class);
        InputData<String> inputLastSecondName = new InputData<>(LAST_SECOND_NAME,setNullIfEquals(value.getSecondLastName(),""));
        results.add(ruleLastSecondName.isValid(inputLastSecondName));

        ExpectPatternRuleValidation ruleRfc = getType(rules.get(RFC),ExpectPatternRuleValidation.class);
        InputData<String> inputRfc = new InputData<>(RFC,value.getRfc());
        results.add(ruleRfc.isValid(inputRfc));

        ExpectPatternRuleValidation rulePhone = getType(rules.get(PHONE),ExpectPatternRuleValidation.class);
        InputData<String> inputPhone = new InputData<>(PHONE,value.getPhone());
        results.add(rulePhone.isValid(inputPhone));

        ExpectPatternRuleValidation ruleExtension = getType(rules.get(EXTENSION),ExpectPatternRuleValidation.class);
        InputData<String> inputExtension = new InputData<>(EXTENSION,setNullIfEquals(value.getExtension(),""));
        results.add(ruleExtension.isValid(inputExtension));

        ExpectPatternRuleValidation ruleEmail = getType(rules.get(EMAIL),ExpectPatternRuleValidation.class);
        InputData<String> inputEmail= new InputData<>(EMAIL,value.getEmail());
        results.add(ruleEmail.isValid(inputEmail));

        NonNullRuleValidation ruleIdBranch = getType(rules.get(ID_BRANCH),NonNullRuleValidation.class);
        InputData<Object> inputIdBranch= new InputData<>(ID_BRANCH,value.getIdBranch());
        results.add(ruleIdBranch.isValid(inputIdBranch));
    }

    private void addExecutiveNumberValidation(List<ResultValidation> results, Long executiveNumber){

        if(kycExecutiveRepository.findById(executiveNumber).isPresent()){
            results.add(ResultValidation.of(true));
        }
        else{
            results.add(ResultValidation.builder()
                    .valid(false)
                    .field("id")
                    .error("Executive id non-existent")
                    .build());
        }
    }
}
