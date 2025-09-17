#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void concat(char* a, char* b, char* out) {
  while (*a != '\0') *out++ = *a++;
  while (*b != '\0') *out++ = *b++;
  *out = '\0';
}

void reverse(char* s, char* out) {
  
  int len = strlen(s)-1;
  while (len >= 0)  *out++ = s[len--];
  *out = '\0';
}
  

int main() {
  char *a = "Hello, ";
  char *b = "world!";
  char out1[256];
  concat(a, b, out1);
  printf("%s\n", out1);

  char str[] = "looc tah\0";
  char out2[256];
  reverse(str, out2);
  printf("%s\n", out2);

  return 0;
}