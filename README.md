# Simulation de Nuée d'Oiseaux

Ce projet a été réalisé dans le cadre de la première année d'école d'ingénieur. Il s'agit d'une simulation interactive de vol d'oiseaux en nuée (flocking behavior) implémentée en Java, basée sur les règles de Boids de Craig Reynolds.

## Description

La simulation modélise le comportement collectif d'oiseaux en vol en utilisant trois règles principales :
- **Séparation** (Répulsion) : Les oiseaux évitent de s'approcher trop près les uns des autres
- **Alignement** : Les oiseaux tendent à s'aligner dans la même direction que leurs voisins
- **Cohésion** (Attraction) : Les oiseaux se dirigent vers le centre de masse de leurs voisins

Le projet inclut également des fonctionnalités avancées :
- Génération procédurale de terrain avec l'algorithme de Perlin Noise
- Interface graphique interactive permettant de modifier les paramètres en temps réel
- Support de plusieurs espèces d'oiseaux avec des comportements distincts
- Système de quadrillage optimisé pour la détection des voisins
- Gestion des collisions et des obstacles

## Fonctionnalités

- **Contrôles interactifs** :
  - Ajustement du nombre d'oiseaux
  - Modification des coefficients de répulsion, alignement et attraction
  - Contrôle de la vitesse de simulation
  - Pause/Reprise de la simulation
  - Ajustement des rayons d'influence pour chaque règle

- **Visualisation** :
  - Affichage en temps réel du mouvement des oiseaux
  - Représentation du terrain avec différentes altitudes
  - Grille de détection des voisins
  - Différenciation visuelle des espèces d'oiseaux

## Structure du Projet

Le projet est organisé en plusieurs classes principales :
- `SimulationNueeOiseaux` : Classe principale et interface utilisateur
- `NueeOiseaux` : Gestion de la nuée et application des règles de comportement
- `Oiseaux` : Représentation et comportement individuel des oiseaux
- `Carte` : Génération et affichage du terrain
- `Grille` : Système de quadrillage pour l'optimisation
- `Cases` : Unités de la grille pour la détection de voisinage

## Branches du Projet

- **Branch `main`** : Version originale du projet rendue pour la deadline. Cette version contient l'implémentation initiale réalisée en groupe.

- **Branch `edit-PA`** : Version personnelle améliorée après la deadline. Cette branche contient plusieurs améliorations :
  - Optimisation des performances pour gérer un plus grand nombre d'oiseaux
  - Amélioration de la génération de terrain avec Perlin Noise
  - Interface utilisateur plus intuitive
  - Meilleure gestion des collisions
  - Code restructuré et commenté
  - Correction de bugs mineurs

## Installation et Exécution

1. Assurez-vous d'avoir Java installé sur votre système
2. Clonez le repository
3. Exécutez le script approprié :
   - Sur Unix/Mac : `./compile_and_run.sh`
   - Sur Windows : `compile_and_run.bat`

## Auteurs

Ce projet a été réalisé en groupe dans le cadre d'un projet de première année d'école d'ingénieur.

## Aspects Techniques

- Utilisation de Java Swing pour l'interface graphique
- Implémentation de l'algorithme de Perlin Noise pour la génération de terrain
- Optimisation des calculs de voisinage avec un système de grille
- Gestion des vecteurs 2D et 3D pour les calculs de mouvement
- Utilisation de structures de données optimisées (ArrayList, HashMap)

## Notes de Développement

- Le projet utilise des opérations non vérifiées (unchecked operations) pour certaines collections
- Les paramètres de simulation sont ajustables pour trouver le meilleur équilibre entre réalisme et performance
- Le système de collision est optimisé pour gérer un grand nombre d'oiseaux

# Simulation de Nuée d'Oiseaux

Ce projet a été réalisé dans le cadre de la première année d'école d'ingénieur. Il s'agit d'une simulation interactive de vol d'oiseaux en nuée (flocking behavior) implémentée en Java, basée sur les règles de Boids de Craig Reynolds.

