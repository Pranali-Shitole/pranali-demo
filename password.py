import re
def is_valid_password(password):
 if 6 <= len(password) <= 12: 
  if re.match(r"^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$#@])", p:
 return True
 return False
 valid_passwords = []
 passwords = input("Enter passwords separated by commas: ").split(',')
 for psw in passwords:
  if is_valid_password(psw):
   valid_passwords.append(psw)
 print(','.join(valid_passwords))