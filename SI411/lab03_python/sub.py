## Toren Hawk
## Lab3, Part 2, sub.py

import re

# Open output file
fout = open("mids.txt", "w")

# Read input file line by line
with open("letters.txt", "r") as fin:
    for line in fin:
        # Look for pattern with regex
        pattern = re.compile("[A-Z]*M[A-Z]*I[A-Z]*D[A-Z]*S[A-Z]*")
        value = line.strip()
        # add to output if match and make replacements
        if re.search(pattern, value):
            value = re.sub("M", "m", value)
            value = re.sub("I", "i", value)
            value = re.sub("D", "d", value)
            value = re.sub("S", "s", value)
            fout.write(value + "\n")

fout.close()