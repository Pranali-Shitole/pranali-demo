class Person:
    def __init__(self, name, age):
        self.name = name      
        self.age = age        

person1 = Person("Alice", 30)
person2 = Person("Bob", 25)

print(person1.name)  
print(person2.name) 
class Example:
    class_variable = "I'm a class variable"  

    def __init__(self, instance_value):
        self.instance_variable = instance_value  

obj1 = Example("Value 1")
obj2 = Example("Value 2")

print(obj1.instance_variable)  
print(obj2.instance_variable)  
print(obj1.class_variable)     

