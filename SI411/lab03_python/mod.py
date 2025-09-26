## Toren Hawk
## Lab3, Part 1, mod.py

f0 = open("mod0.txt", "w")
f1 = open("mod1.txt", "w")
f2 = open("mod2.txt", "w")

with open("numbers.txt", "r") as fin:
    for line in fin:
        value = int(line.strip())
        if value % 3 == 0:
            f0.write(f"{value}\n")
        elif value % 3  == 1:
            f1.write(f"{value}\n")
        elif value % 3 == 2:
            f2.write(f"{value}\n")