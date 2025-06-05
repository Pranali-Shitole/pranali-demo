class Shape:
 def __init__(self):
  pass  
 def area(self):
  return 0
 class Square(Shape):
  def __init__(self, length):
   super().__init__()  
 self.length = length
 def area(self):
  return self.length ** 2
  shape = Shape()
 square = Square(float(input("Enter the shape of the square: ")))
 print("Shape's area by default:", shape.area()) 
print("Area of the square:", square.area()) 