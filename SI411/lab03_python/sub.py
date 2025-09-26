import re

fout = open("mids.txt", "w")

with open("letters.txt", "r") as fin:
    for line in fin:
        value = line.strip()
        if re.search(".*M.*I.*D.*S.*", value):
            output = re.sub("[MIDS]", "mids", value)
            fout.write(output)