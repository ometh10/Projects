#ifndef GRAPH_HPP_
#define GRAPH_HPP_

#include <iostream>
#include <fstream>
#include <utility>
#include <functional>
#include <vector>
#include <string>
#include <queue>
#include <unordered_map>
#include <limits>

template <typename T>
class Graph {
 private:
  std::vector<std::unordered_map<int, T> > adjList {};
  int numVertices {};

 public:
  // empty graph with N vertices
  explicit Graph(int N);

  // construct graph from edge list in filename
  explicit Graph(const std::string& filename);

  // add an edge directed from vertex i to vertex j with given weight
  void addEdge(int i, int j, T weight);

  // removes edge from vertex i to vertex j
  void removeEdge(int i, int j);

  // is there an edge from vertex i to vertex j?
  bool isEdge(int i, int j) const;

  // return weight of edge from i to j
  // will throw an exception if there is no edge from i to j
  T getEdgeWeight(int i, int j) const;

  // returns number of vertices in the graph
  int size() const;

  // return iterator to a particular vertex
  const std::unordered_map<int, T>& neighbours(int a) const {
    return adjList.at(a);
  }
};

template <typename T>
Graph<T>::Graph(int N) : adjList(N), numVertices {N} {}

template <typename T>
Graph<T>::Graph(const std::string& inputFile) {
  std::ifstream infile {inputFile};
  if (!infile) {
    std::cerr << inputFile << " could not be opened\n";
    return;
  }
  // first line has number of vertices
  infile >> numVertices;
  adjList.resize(numVertices);
  int i {};
  int j {};
  double weight {};
  // assume each remaining line is of form
  // origin dest weight
  while (infile >> i >> j >> weight) {
    addEdge(i, j, static_cast<T>(weight));
  }
}

template <typename T>
int Graph<T>::size() const {
  return numVertices;
}

template <typename T>
void Graph<T>::addEdge(int i, int j, T weight) {
  if (i < 0 or i >= numVertices or j < 0 or j >= numVertices) {
    throw std::out_of_range("invalid vertex number");
  }
  adjList[i].insert({j, weight});
}

template <typename T>
void Graph<T>::removeEdge(int i, int j) {
  // check if i and j are valid
  if (i >= 0 && i < numVertices && j >= 0 && j < numVertices) {
    adjList[i].erase(j);
  }
}

template <typename T>
bool Graph<T>::isEdge(int i, int j) const {
  if (i >= 0 && i < numVertices && j >= 0 && j < numVertices) {
    return adjList.at(i).contains(j);
  }
  return false;
}

template <typename T>
T Graph<T>::getEdgeWeight(int i, int j) const {
  return adjList.at(i).at(j);
}

template <typename T>
std::ostream& operator<<(std::ostream& out, const Graph<T>& G) {
  for (int i = 0; i < G.size(); ++i) {
    out << i << ':';
    for (const auto& [neighbour, weight] : G.neighbours(i)) {
      out << " (" << i << ", " << neighbour << ")[" << weight << ']';
    }
    out << '\n';
  }
  return out;
}


// APSP functions
// Use this function to return an "infinity" value
// appropriate for the type T
template <typename T>
T infinity() {
  if (std::numeric_limits<T>::has_infinity) {
    return std::numeric_limits<T>::infinity();
  } else {
    return std::numeric_limits<T>::max();
  }
}

// implement an algorithm for determining if G
// has a negative weight cycle here
template <typename T>
bool existsNegativeCycle(const Graph<T>& G) {
  // number of vertices in the graph
  int n = G.size();
  // assigning distance 0 to all the vertices from start, in order indentify the 
  std::vector<T> distance(n,0);

  // relaxing all the edges 
  for(int i = 0; i < n - 1; ++i){
    // itarating through all the vertices in the graph
    for(int u = 0; u < n; ++u){
      // check the distance and relaxing all the neighbours of vertex uniform_int_distribution
      for(const auto& [v, weight] : G.neighbours(u)){
        // relaxing happens in each iteration when it found a minimum cost path 
        if(distance.at(u) + weight < distance.at(v)){
          distance.at(v) = distance.at(u) + weight;
        }
      }
    }
  }

  // iterate again through the each vertice's neighbours to find the negative weight cycle
  for(int u = 0; u < n; ++u){
    // checking the neighbouring vertices of u
    for(const auto& [v,weight] : G.neighbours(u)){
      // after the above relaxing yet still finds a shortest path means there is a negative weight cycle
      if(distance.at(u) + weight < distance.at(v)){
        return true;
      }
    }
  }
  return false;
}

// implement Johnson's APSP algorithm here
// https://www.youtube.com/watch?v=MV7EAD9zL64 (reference for the johnsonAPSP algorithm)
template <typename T>
std::vector<std::vector<T> >
johnsonAPSP(const Graph<T>& G) {
  int n = G.size();

  // if negative cycle exists return
  if(existsNegativeCycle(G)){
    return std::vector<std::vector<T>>{};
  }

  // running bellman ford to compute the h[v]
  std::vector<T> h(n, 0);

  // relaxing the edges
  for(int i = 0; i < n - 1; ++i){
    for(int u = 0; u < n; ++u){
      for(const auto& [v, weight] : G.neighbours(u)){
        if(h[u] != infinity<T>() && h[v] > h[u] + weight){
          h[v] = h[u] + weight;
        }
      }
    }
  }

  // run Dijkstra from every vertex in G 
  // while running it reweighting the edges
  std::vector<std::vector<T>> distance(n, std::vector<T>(n, infinity<T>()));

  for(int src = 0; src < n; ++src){
    using distAndVertex = std::pair<T, int>;
    std::priority_queue<distAndVertex, std::vector<distAndVertex>, std::greater<distAndVertex>> pq;
    distance[src][src] = 0;
    pq.push({0,src});

    while(!pq.empty()){
      auto [du, u] = pq.top();
      pq.pop();

      if(du <= distance[src][u]){
        for(const auto& [v, weight] : G.neighbours(u)){
          if(h[u] != infinity<T>() && h[v] != infinity<T>()){
             T reWeight = weight + h[u] - h[v];

            if(distance[src][v] > distance[src][u] + reWeight){
              distance[src][v] = distance[src][u] + reWeight;
              pq.push({distance[src][v], v});
            }
          }
        }
      }
    }
  }

  for(int u = 0; u < n; ++u){
    for(int v = 0; v < n; ++v){
      if(distance[u][v] != infinity<T>()){
        distance[u][v] = distance[u][v] - h[u] + h[v];
      }
    }
  }

  return distance;
}

// implement the Floyd-Warshall APSP algorithm here
template <typename T>
std::vector<std::vector<T> >
floydWarshallAPSP(const Graph<T>& G) {
  int n = G.size();

  // https://www.geeksforgeeks.org/2d-vector-in-cpp-with-user-defined-size/
  // initializing the 2D vector (above is the reference I used)
  std::vector<std::vector<T>> distanceMatrix(n, std::vector<T>(n, infinity<T>()));

  // mapping the graph into the matrix
  for(int i = 0; i < n; ++i){
    for(int j = 0; j < n; ++j){
      // when i = j setting the edge weight to 0
      if(i == j){
        distanceMatrix[i][j] = 0;
      // if it has a directed edge assign the edge
      }else if(G.isEdge(i,j)){
        distanceMatrix[i][j] = G.getEdgeWeight(i,j);
      // if it doesnt have directed edge set to infinity
      }else{
        distanceMatrix[i][j] = infinity<T>();
      }
    }
  }

  // https://www.youtube.com/watch?v=oNI0rf2P9gE&t=1s
  // using triple nested loop and updating the shortest path of every pair
  for(int k = 0; k < n; ++k){
    for(int i = 0; i < n; ++i){
      for(int j = 0; j < n; ++j){
        // matrix distance of i to k
        T ik = distanceMatrix[i][k];
        // matrix distance of k to j
        T kj = distanceMatrix[k][j];

        // only if ik or kj is not equal to infinity calculate the new distance
        if(ik != infinity<T>() && kj != infinity<T>()){
          // adding ik and kj and assigning it to newDistance
          T newDistance = ik + kj;
          //if the new distance is less assigning the new distance as the distance of the i and j
          if(distanceMatrix[i][j] > newDistance){
            distanceMatrix[i][j] = newDistance;
          }
        } 
      }
    }
  }

  return distanceMatrix;
}

#endif      // GRAPH_HPP_
