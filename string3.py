def num_layers(n):
 initial_thickness_mm = 0.5  # Initial thickness in millimeters
 final_thickness_mm = initial_thickness_mm * (2 ** n)
 final_thickness_m = final_thickness_mm / 1000  # Convert millimeter
 return f"{final_thickness_m:.3f}m"
num_layers(1)
num_layers(4)
num_layers(21)