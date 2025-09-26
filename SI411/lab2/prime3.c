
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdbool.h>

#define NTHREADS 5
#define MAX_SIZE 1000 		// How many to read
#define FILENAME "numbers.txt"

void* check_primes();

void printResults();

// Global variables (accessible by all threads)
int numbers[MAX_SIZE];           // will be read from file
int primesFound[MAX_SIZE];       // will hold the primes that are found
int primesFoundCount = 0;        // how many primes found so far??
pthread_mutex_t lock;

typedef struct {
  int t_num;
  int t_prime;
} thread_data;

int main()
{
  pthread_mutex_init(&lock, NULL);

  int i;
  // Ensure memory is empty to start
  for (i=0; i<MAX_SIZE; i++) {
    primesFound[i] = 0;
  }
  
  // Read numbers from file, store into array
  FILE * file = fopen (FILENAME, "r");
  if (file == NULL) {
    //printf("Error opening %s\n", FILENAME);
    exit(1);
  }
  i = 0;
  while ((!feof(file)) && (i < MAX_SIZE)) {  
    fscanf (file, "%d", &numbers[i]);    
    i++;
  }
  fclose (file);        
  
  // TODO: create the threads, and have each one execute 'check_primes'
  pthread_t thread_id[NTHREADS];
  long t_num;  // must be 'long' vice int to make easier to pass as arg to pthread_create


  // Create the threads, passing 't_num' as argument to thread (becomes the thread number)
  thread_data thread_args[NTHREADS];
  for(t_num=0; t_num < NTHREADS; t_num++) {
    thread_args[t_num].t_num = t_num;
    thread_args[t_num].t_prime = 0;
    pthread_create( &thread_id[t_num], NULL, check_primes, &thread_args[t_num]);
  }

  // TODO: join threads when complete
  // Join all threads when complete
  for(int j=0; j < NTHREADS; j++) {
    pthread_join(thread_id[j], NULL);
  }


  // Print the results
  for (i=0; i<NTHREADS; i++) {
    //printf("Thread %d found %d primes\n", i, thread_args[i].t_prime);
  }
  printf ("Found a total of %d primes.\n", primesFoundCount);
  // TODO (later!): uncomment printResults() when instructed to do so
  printResults();  
}

// Print out all the primes that were found
void printResults() {
  int i;
  for (i=0; i<primesFoundCount; i++) {
    //printf("%d ", primesFound[i]);
  }
  //printf ("\n");
}

// Returns true if 'num' is prime
bool isPrime(int num) {
  if (num <= 1) return false;
  for (int j = 2; j < num; j++) {
    if (num % j == 0) {
      return false;
    }
  }
  return true;  // no factor found, so must be prime
}

// Check all values in the array numbers[], and place any primes found in the primesFound[] array.
void* check_primes(thread_data* t)
{
  // TODO: make any necessary changes so that each thread can call this function AND
  //   have the 'work' divided evenly among the threads.
  //   Do NOT make multiple copies of any code!
  //   Also, your code must work work any value of NTHREADS
  
  int num = 0;
  long i = (long) t->t_num;
  

  while (i<MAX_SIZE) {
    num = numbers[i];
    if (isPrime(num)) {
      pthread_mutex_lock(&lock);
      primesFound[primesFoundCount] = num;
      //printf("found %d \n",num);
      primesFoundCount++;
      pthread_mutex_unlock(&lock);
      t->t_prime++;
    }
    i+=NTHREADS;
  }
  return 0;
}
