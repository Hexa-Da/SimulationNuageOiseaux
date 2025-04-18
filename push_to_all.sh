#!/bin/bash

# Pousser vers le dépôt origin (Hexa-Da)
echo "Pushing to Hexa-Da repository..."
git push origin edit-PA
git push origin main

# Pousser vers le dépôt jafaar
echo "Pushing to Jafaar repository..."
git push jafaar edit-PA
git push jafaar main

echo "Push completed to both repositories!" 