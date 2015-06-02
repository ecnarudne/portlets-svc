
$('#defaultForm').formValidation
  message: 'This value is not valid'
  icon:
    required: 'fa fa-asterisk'
    valid: 'fa fa-check'
    invalid: 'fa fa-times'
    validating: 'fa fa-refresh'
  fields:
    email: validators:
      notEmpty: message: 'The email address is required'
      emailAddress: message: 'The input is not a valid email address'
    pwdName: validators:
      notEmpty: message: 'The password is required'

