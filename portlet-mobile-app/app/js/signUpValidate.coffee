
$('#defaultForm').formValidation
  message: 'This value is not valid'
  icon:
    required: 'fa fa-asterisk'
    valid: 'fa fa-check'
    invalid: 'fa fa-times'
    validating: 'fa fa-refresh'
  fields:
    username:
      message: 'The username is not valid'
      validators: notEmpty: message: 'The username is required'
    email: validators:
      notEmpty: message: 'The email address is required'
      emailAddress: message: 'The input is not a valid email address'
    pwdName: validators:
      notEmpty: message: 'The password is required'
      identical:
        field: 'cpwdName'
        message: 'The password and its confirm are not the same'
    cpwdName: validators:
      notEmpty: message: 'The confirm password is required'
      identical:
        field: 'pwdName'
        message: 'The password and its confirm are not the same'
