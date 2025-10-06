#include <stdlib.h>
#include <string.h>

void reverse_string(char* s, char* out) {
  int len = strlen(s)-1;
  while (len >= 0)  *out++ = s[len--];
  *out = '\0';
}

int reverse_bool(int original) {
  if (original == 0) return 1;
  else return 0;
}

void concat(char* a, char* b, char* out) {
  while (*a != '\0') *out++ = *a++;
  while (*b != '\0') *out++ = *b++;
  *out = '\0';
}
