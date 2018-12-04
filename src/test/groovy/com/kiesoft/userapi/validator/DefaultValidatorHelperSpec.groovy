package com.kiesoft.userapi.validator

import org.springframework.validation.BeanPropertyBindingResult
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DefaultValidatorHelperSpec extends Specification {

    final validatorHelper = new DefaultValidatorHelper()

    def "will reject the string '#string' if null, empty or whitespaces"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(string)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIfStringIsBlank("stringField", string, errors)

        then:
        errors.getErrorCount() == 1

        where:
        string || _
        null   || _
        ""     || _
        "   "  || _
    }

    def "will not reject a String when is not null, empty or whitespaces"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(string)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIfStringIsBlank("stringField", string, errors)

        then:
        !errors.hasErrors()

        where:
        string                                                                                               || _
        "0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz !\"#\$%&'()*+,-./:;<=>?[\\^_{|}~@" || _
    }

    def "will reject the string '#string' if length not between #minLength and #maxLength"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(string)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectStringIfNotInLength("stringField", string, minLength, maxLength, errors)

        then:
        errors.hasErrors()

        where:
        string                     | minLength | maxLength | _
        null                       | 1         | 3         | _
        ""                         | 1         | 3         | _
        "       "                  | 1         | 3         | _
        "Pedro"                    | 6         | 10        | _
        "Pedro Gonzalez Gutierrez" | 6         | 10        | _
    }

    def "will not reject the string '#string' if length between #minLength and #maxLength"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(string)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectStringIfNotInLength("stringField", string, minLength, maxLength, errors)

        then:
        !errors.hasErrors()

        where:
        string  | minLength | maxLength || _
        "A"     | 1         | 3         || _
        "Pedro" | 3         | 15        || _
        "AAA"   | 1         | 3         || _
    }

    def "will reject the integer '#integer' if value not between #minValue and #maxValue"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setIntegerField(integer)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIntegerIfNotInRange("integerField", integer, minValue, maxValue, errors)

        then:
        errors.getErrorCount() == 1

        where:
        integer | minValue | maxValue || _
        null    | 10       | 20       || _
        5       | 10       | 20       || _
        25      | 10       | 20       || _
    }

    def "will not reject the integer '#integer' if value between #minValue and #maxValue"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setIntegerField(integer)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIntegerIfNotInRange("integerField", integer, minValue, maxValue, errors)

        then:
        !errors.hasErrors()

        where:
        integer | minValue | maxValue || _
        5       | 5        | 20       || _
        15      | 5        | 20       || _
        20      | 5        | 20       || _
    }

    def "will reject the boolean if is null"() {
        given:
        final validationObject = new ValidationObject()
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectBooleanIfNull("booleanField", null, errors)

        then:
        errors.hasErrors()
    }

    def "will not reject the boolean when is '#booleanValue'"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setBooleanField(booleanValue)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectBooleanIfNull("booleanField", booleanValue, errors)

        then:
        !errors.hasErrors()

        where:
        booleanValue  || _
        Boolean.TRUE  || _
        Boolean.FALSE || _
    }

    def "will remove HTML and Javascript code from string '#string'"() {
        when:
        final actualString = validatorHelper.removeHTMLandJS(untrustedString)

        then:
        actualString == expectedString

        where:
        untrustedString                                       || expectedString
        "<a>Hola</a>"                                         || "Hola"
        "<script>alert(1)</script>"                           || ""
        "<p><script>alert(1)</script>Real Betis Balompie</p>" || "Real Betis Balompie"
    }

    def "will reject the invalid email '#untrustedEmail'"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(untrustedEmail)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIfNotEmail("stringField", untrustedEmail, errors)

        then:
        errors.getErrorCount() == 1

        where:
        untrustedEmail || _
        null           || _
        ""             || _
        "     "        || _
        "correo"       || _
        "1000 aaaa"    || _
        "correo@"      || _
        "correo@loco"  || _
        "@loco.com"    || _
    }

    def "will accept the email '#untrustedEmail'"() {
        given:
        final validationObject = new ValidationObject()
        validationObject.setStringField(untrustedEmail)
        final errors = new BeanPropertyBindingResult(validationObject, "validationObject")

        when:
        validatorHelper.rejectIfNotEmail("stringField", untrustedEmail, errors)

        then:
        errors.getErrorCount() == 0

        where:
        untrustedEmail           || _
        "pedro@yahoo.com"        || _
        "macarena@gmail.es"      || _
        "asuncion@hotmail.co.uk" || _
    }

    private static class ValidationObject {
        private String stringField;
        private Integer integerField;
        private Boolean booleanField;

        String getStringField() {
            return stringField
        }

        void setStringField(String stringField) {
            this.stringField = stringField
        }

        Integer getIntegerField() {
            return integerField
        }

        void setIntegerField(Integer integerField) {
            this.integerField = integerField
        }

        Boolean getBooleanField() {
            return booleanField
        }

        void setBooleanField(Boolean booleanField) {
            this.booleanField = booleanField
        }

    }

}
