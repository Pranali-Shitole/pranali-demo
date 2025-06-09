def society_name(names):
 secret_name = ''.join(sorted([name[0] for name in names]))
 return secret_name
society_name(["Adam", "Sarah", "Malcolm"])
society_name(["Harry", "Newt", "Luna", "Cho"])
society_name(["Phoebe", "Chandler", "Rachel", "Ross", "Monica", "Joey"])