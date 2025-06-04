#include <utility>
#include <list>
#include "hash.hpp"


HashSet::Iterator HashSet::begin() {
  return elements.begin();
}

HashSet::Iterator HashSet::end()  {
  return elements.end();
}

// Constructor
HashSet::HashSet():
  sizeIndex(0),
  numBuckets (sizes[sizeIndex]),
  numElements(0),
  maxLoad(1.0f)
  {
     buckets = new std::list<int>::iterator [numBuckets];

     for(std::size_t i = 0; i < numBuckets; ++i){
        buckets[i] = elements.end();
     }
  }

// Copy Constructor
HashSet::HashSet(const HashSet& other) :
  sizeIndex(other.sizeIndex),
  elements(other.elements),
  numBuckets(other.numBuckets),
  numElements(other.numElements),
  maxLoad(other.maxLoad)
  {
    // new memory for for buckets
    buckets = new std::list<int>::iterator [numBuckets];

    // point all the buckets to the end of the list
    for(std::size_t i = 0; i < numBuckets; ++i){
      buckets[i] = elements.end();
    }

    // mapping the elements and assigning the buckets with the iterators
    for(auto it = elements.begin(); it != elements.end(); ++it){
      std::size_t b = bucket(*it);
      if(buckets[b] == elements.end()){
        buckets[b] = it;
      }
    }
  }

// Assignment constructor
HashSet& HashSet::operator=(HashSet other) {
  std::swap(sizeIndex, other.sizeIndex);
  std::swap(elements, other.elements);
  std::swap(buckets, other.buckets);
  std::swap(numElements, other.numElements);
  std::swap(numBuckets, other.numBuckets);
  std::swap(maxLoad, other.maxLoad);
  return*this;
}

// destructor
HashSet::~HashSet() { 
  delete[] buckets;
}

void HashSet::insert(int key) {
  
  // check the key is already in the list
  if(contains(key)){
    return;
  }
    
    //Determining the bucket according to the key
    std::size_t bucketIndex = bucket(key);
    std::list<int>::iterator newNodeIt;
    // insert the key into the list if the bucket doesnt pointing at node in the list
    if(buckets[bucketIndex] == elements.end()){
      // if the bucket is empty adding the new key to the end of the elements list
      elements.push_back(key);
      // adding the iterator to the newly added key
      newNodeIt = std::prev(elements.end());
      // passing the newNodeIt to the bucket iterator
      buckets[bucketIndex] = newNodeIt; 
    }else{
      // when the bucket has a chain, finds the last element of the bucket's chain
      // set a iterator to start at the bucket's first element
      auto it = buckets[bucketIndex];
      while(std::next(it) != elements.end() && bucket(*std::next(it)) == bucketIndex){
        // iterator to move the next key of the chain
        it = std::next(it);
      }
      // insert the key after the last key of the chain
      newNodeIt = elements.insert(std::next(it), key);
    }
    ++numElements;

    // Check the loadFactor and if resize needed
    if(loadFactor() > maxLoad && sizeIndex + 1 < sizes.size()){
      ++sizeIndex;
      rehash(sizes[sizeIndex]);
    }
}


bool HashSet::contains(int key) const {
  // getting the bucket index for the key
  std::size_t bucketIndex = bucket(key);

  // gettting the iterator of the relevent bucket 
  auto it = buckets[bucketIndex];

  while(it != elements.end() && bucket(*it) == bucketIndex){
    if(*it == key){
      return true;
    }
    it = std::next(it);
  }
  return false;
}

HashSet::Iterator HashSet::find(int key) {
  // Hash the key to get the bucket index
  std::size_t bucketIndex = bucket(key);

  // If the bucket's iterator is not pointing to the end of the list find the key
  if(buckets[bucketIndex] != elements.end()){
    // map through the relevent bucket's chain and fin the key
    auto it = buckets[bucketIndex];
    while(it != elements.end() && bucket(*it) == bucketIndex){
      if(*it == key){
        // key found
        return it; 
      }
      // moving to the next element in the chain
      it = std::next(it);
    }
  }
  // not found
  return elements.end();
}

void HashSet::erase(int key) {
  // finds the iterator for the key
  auto it = find(key);

  // pointer points to the end of the list
  if(it == elements.end()){
    return;
  }

    std::size_t bucketIndex = bucket(key);

    // auto nextIt = elements.erase(it);
    // if removing the first element in the list and handles it
    if(buckets[bucketIndex] == it){
      // get to the next element
      auto nextIt = std::next(it);
      // point the iterator at the next element if the next element in the same bucket
      // if not pointing to the iterator to the end
      if(nextIt != elements.end() && bucket(*nextIt)== bucketIndex){
        buckets[bucketIndex] = nextIt;
      }else{
        buckets[bucketIndex] = elements.end();
      }
    }
    elements.erase(it);
    --numElements;
}

HashSet::Iterator HashSet::erase(HashSet::Iterator it) {

  // check the iterator pointing at the list to continue the operation
  if(it == elements.end()){
    return it;
  }
  
    // getting the bucket index
    std::size_t bucketIndex = bucket(*it);
    auto nextIt = std::next(it);

    // if erasing the first element in the chain
    if(buckets[bucketIndex] == it){
      // if there is a next element point the iterator to that element
      if(nextIt != elements.end() && bucket(*nextIt) == bucketIndex){
        buckets[bucketIndex] = nextIt;
      }else{
        // if not then point to the end of the list
        buckets[bucketIndex] = elements.end();
      }
    }

      elements.erase(it);
      --numElements;
      return nextIt;
}

// Note: I have found where the main error was, it was in the bucket function. when handling the negative
// values it gives out a large number so I used std::hash. Also, reduced for loops inside the rehash and 
// do it with an one loop.
void HashSet::rehash(std::size_t newSize) {
  
  // Creating a new bucket array for the newSize
  auto* newBuckets = new std::list<int>::iterator[newSize];
  // pointing all the iterators to the end
  for(std::size_t i = 0; i < newSize; ++i){
    newBuckets[i] = elements.end();
  }

  // splicing all the current elements to a temp list
  std::list<int> tempElements;
  tempElements.splice(tempElements.end(), elements);

  // updating the numBuckets with the newSize
  numBuckets = newSize;

  // splicing back from tempElements to the element with the updated buckets
  for(auto it = tempElements.begin(); it != tempElements.end(); ){
    std::size_t b = bucket(*it);
    // saving the next element
    auto nextIt = std::next(it);

    if(newBuckets[b] == elements.end()){
      elements.splice(elements.end(), tempElements, it);
      newBuckets[b] = std::prev(elements.end());
    }else{
      auto lastElementInChain = newBuckets[b];
      while(std::next(lastElementInChain) != elements.end() && bucket(*std::next(lastElementInChain)) == b){
        lastElementInChain = std::next(lastElementInChain);
      }
      elements.splice(std::next(lastElementInChain), tempElements, it);
    }

    it = nextIt;
  }

  delete[] buckets;
  buckets = newBuckets;
}



// Utility functions
std::size_t HashSet::size() const {
  return numElements;
}

bool HashSet::empty() const {
  return numElements == 0;
}

std::size_t HashSet::bucketCount() const {
  return numBuckets;
}

std::size_t HashSet::bucketSize(std::size_t b) const {
  // check size is within range
  if(b >= numBuckets || buckets[b] == elements.end()){
    return 0;
  }
    // if the iterator pointing to the end of the list 
    if(buckets[b] == elements.end()){
      return 0;
    }
    // getting the count of the elements in the bucket
    std::size_t sizeCount = 0;
    for(auto it = buckets[b]; it != elements.end() && bucket(*it) == b; ++it) {
      ++sizeCount;
  }
  return sizeCount;
}

std::size_t HashSet::bucket(int key) const{
  // handle nagative values correctly 
  // https://en.cppreference.com/w/cpp/utility/hash
  return std::hash<std::size_t>{}(key) % numBuckets;
}

float HashSet::loadFactor() const {
  if(numBuckets == 0) {
    return 0.0;
  }
  return float(numElements) / numBuckets;
}

float HashSet::maxLoadFactor() const {
  return maxLoad;
}

void HashSet::maxLoadFactor(float maxLoad) {
  this->maxLoad = maxLoad;
    while(loadFactor() > maxLoad && sizeIndex + 1 < sizes.size()){
    ++sizeIndex;
    rehash(sizes[sizeIndex]);
  }

}
