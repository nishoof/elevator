#!/bin/bash
set -e # Exit on error

cd "$(dirname "$0")"
echo "Building Elevator Simulator..."

mkdir -p bin
mkdir -p dist

echo "Compiling Java files..."
javac -cp "lib/core.jar:." -d bin src/Main/*.java src/Screens/*.java src/Elements/*.java src/Elements/Button/*.java

echo "Packaging JAR file..."
mkdir -p temp_processing_classes
cd temp_processing_classes
jar xf ../lib/core.jar
cd ..

jar cfm dist/ElevatorSimulator.jar manifest.txt -C bin . -C temp_processing_classes . -C . data

echo "Cleaning up..."
rm -rf temp_processing_classes

echo "Build complete: dist/ElevatorSimulator.jar"