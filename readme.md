# Shortest Path Visualizer
  - [Description](#description)
  - [Functionality](#functionality)
  - [Motivation](#motivation)
  - [Installation](#installation)
  - [Manual](#manual)
## Description

This is an open-source interactive shortest path algorithm visualizer. It allows user to create his own graph structures and explore various types of algorithms including: Dijkstra, A*, Bellman-Ford, Ant Optimization and Genetic.

![til](./src/main/resources/org/example/readme/app.gif)

## Functionality

## Motivation

I made this application purely for educational purposes. I remember reading about Ant Optimization Algorithm and it quite fascinated me so I was wondering how can it be implemented to solve shortest path problem for graphs. Later I added more algorithms to make this application more complete.


## Installation 

Make sure `Java Runtime Environment` is installed on your system.
Go to releases page and download version of your choice (for example: [latest release](https://github.com/Mich519/shortest-path/releases/latest)). 
Execute `java -jar <name_of_the_release>.jar`.

## User Guide

  Adding new vertex: 
  <ol>
    <li>Make sure 'Add vertex' field is checked in Editor tab. </li>
    <li> Use right click inside editor pane to add new vertex. </li>
  </ol>

  Removing vertex (and corresponding edges):
  <ol>
    <li> Make sure 'Remove vertex' field is checked in Editor tab. </li>
    <li> Use right click inside editor on existing vertex to remove it. (All corresponsing edges will also be removed) </li>
  </ol>

  Adding new edge:
  <ol>
    <li> Click and hold on existing vertex with a middle button. </li>
    <li> Drag the mouse to other vertex and release to create edge between them. </li>
  </ol>

  Running algorithms:
  <ol>
    <li>Create a graph manually, generate new one (Generator tab -> Generate) or load existing one from file (File->Load->Select graph file ...) </li>
    <li> Now select starting vertex for your algorithm. Make sure 'Choose start node' field is checked in editor tab. Now right click on existing vertex. It should now be marked with green color. </li>
    <li> Now select destination vertex. Make sure 'Choose end node' field is checked in editor tab. Now like in previous step right click on existing vertex and it should be marked with red color. </li>
    <li> Head on to 'Simulation' tab and pick your favourite algorithm. Some of them allow you to change various parameters. Feel free to experiment with it. </li>
    <li> Click on 'Start' button and enjoy the view. </li>
  </ol>