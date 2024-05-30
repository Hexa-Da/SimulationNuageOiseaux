import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SimulationExperience extends JPanel implements ActionListener {

    private Timer timer;
    boolean isPaused = true;

    int largeur;
    int hauteur;

    int tailleCase;
    ArrayList<Case> grille;

    int nombreOiseaux = 100;
    ArrayList<Oiseau> oiseaux;

    // Paramètres oiseau
    double coeffRepulsion = 0.05;
    double coeffAlignement = 0.1;
    double coeffAttraction = 0.05;
    double vitesse = 3;
    int rayonAt = 100;
    int rayonAl = 66;
    int rayonRe = 33;

    Color[][] colors;
    BufferedImage bufferedImage;

    public SimulationExperience(int largeur, int hauteur, int tailleCase) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.tailleCase = tailleCase;

        this.setSize(new Dimension(largeur, hauteur));

        creerGrille();
        creerCarte();
        
        bufferedImage = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);

        // Dessiner la matrice de couleurs dans l'image tampon
        Graphics2D img = bufferedImage.createGraphics();
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                img.setColor(colors[i][j]);
                img.fillRect(j, i, 1, 1);
            }
        }
        img.dispose();

        creerOiseaux();

        // Créer un timer pour mettre à jour la position des oiseaux
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.drawImage(bufferedImage, 0, 0, this);
        
        // Dessiner les oiseaux
        for (Oiseau oiseau : oiseaux) {
            oiseau.dessiner(g2d);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPaused) {
            // Mettre à jour la position des oiseaux
            for (Oiseau oiseau : oiseaux) {
                // Appliquer la méthode boid
                for (Case c : grille) {
                    if (c.numCase == oiseau.numCase) {
                        ArrayList<Oiseau> voisins = new ArrayList<>(c.population);

                        for (Case voisin : c.caseVoisine) {
                            voisins.addAll(voisin.population);
                        }

                        oiseau.repulsion(voisins, coeffRepulsion, vitesse, rayonRe);
                        oiseau.alignement(voisins, coeffAlignement, vitesse, rayonAl, rayonRe);
                        oiseau.attraction(voisins, coeffAttraction, vitesse, rayonAt, rayonAl);
                        oiseau.normerVitesse(vitesse);
                    }
                }

                oiseau.deplacer(vitesse, largeur, hauteur);

                // Actualiser les cases
                double x = oiseau.x;
                double y = oiseau.y;
                int numCase = oiseau.numCase;

                if (coord_to_num(x, y) != numCase) {
                    oiseau.numCase = coord_to_num(x, y);
                    for (Case c : grille) {
                        if (c.numCase == oiseau.numCase) {
                            c.population.add(oiseau);
                        }
                    }

                    for (Case c : grille) {
                        if (c.numCase == numCase) {
                            c.population.remove(oiseau);
                        }
                    }
                }
            }

            repaint(); 

        }
    }

    private void creerGrille() {
        this.grille = new ArrayList<>();
        int nombreLignes = hauteur / tailleCase;
        int nombreColonnes = largeur / tailleCase;

        // Création des cases du quadrillage
        for (int i = 0; i < nombreLignes; i++) {
            for (int j = 0; j < nombreColonnes; j++) {
                int numCase = i * nombreColonnes + j; // 0 à N-1
                // Coordonnées du point en haut à gauche de chaque case
                // xCase abscisse et yCase ordonnée, origine en haut à gauche
                int xCase = j * tailleCase;
                int yCase = i * tailleCase;

                Case nouvelleCase = new Case(numCase, xCase, yCase, tailleCase);
                this.grille.add(nouvelleCase);
            }
        }
    }

    private void creerCarte(){
        Vecti gridSize = new Vecti(largeur,hauteur);
		Vecti size = new Vecti(3,3); //On divise notre carte en une grille de 3 par 3
        long seed = (long) aleatoire(2<<10,2<<99); //Génère une seed aléatoire
        colors = mapGeneration(seed,gridSize,size);
    }

    private void creerOiseaux() {
        oiseaux = new ArrayList<>();

        for (int i = 0; i < nombreOiseaux; i++) {
            double x = Math.random() * largeur;
            double y = Math.random() * hauteur;
            int numCase = coord_to_num(x, y);
            Oiseau oiseau = new Oiseau(x, y, numCase, i);
            oiseaux.add(oiseau);

            // On initialise la population de chaque case
            for (Case c : grille) {
                if (c.numCase == numCase) {
                    c.population.add(oiseau);
                }
            }
        }
    }

    private int coord_to_num(double x, double y) {
        int xCase = (int) x / tailleCase;
        int yCase = (int) y / tailleCase;
        return yCase * (largeur / tailleCase) + xCase;
    }

    public void setPause() {
        this.isPaused = !this.isPaused;
    }

    public void setRayonAt(int r) {
        this.rayonAt = r;
    }

    public void setRayonAl(int r) {
        this.rayonAl = r;
    }

    public void setRayonRe(int r) {
        this.rayonRe = r;
    }

    public void setNbrOiseaux(int nombreOiseaux) {
        this.nombreOiseaux = nombreOiseaux;
        this.creerOiseaux();
    }

    public void setvitesse(double vitesse) {
        this.vitesse = vitesse;
    }

    public void setRepulsion(double coeffRepulsion) {
        this.coeffRepulsion = coeffRepulsion;
    }

    public void setAlignement(double coeffAlignement) {
        this.coeffAlignement = coeffAlignement;
    }

    public void setAttraction(double coeffAttraction) {
        this.coeffAttraction = coeffAttraction;
    }

    public static double perlin(double x,double y, int[] permutation){
   
        double unit = 1.0/Math.sqrt(2.);
        double gradientList[][] = {{unit,unit},{-unit,unit},{unit,-unit},{-unit,-unit},{1.,0.},{-1.,0.},{0.,1.},{0.,-1.}};
  
        int x0 = (int)Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int)Math.floor(y);
        int y1 = y0 + 1;
        
        int xx = x0&255; //Donne une valeur entre 0 et 255
        int yy = y0&255;
        
        int aleaHG = permutation[xx + permutation[yy]] % 8;
        Vectf gradHG = new Vectf(gradientList[aleaHG][0],gradientList[aleaHG][1]);
            	
        int aleaHD = permutation[xx + 1 + permutation[yy]] % 8;
        Vectf gradHD = new Vectf(gradientList[aleaHD][0],gradientList[aleaHD][1]);
            	
        int aleaBG = permutation[xx + permutation[yy + 1]] % 8;
        Vectf gradBG = new Vectf(gradientList[aleaBG][0],gradientList[aleaBG][1]);	
            	
        int aleaBD = permutation[xx + 1 + permutation[yy + 1]] % 8;
        Vectf gradBD = new Vectf(gradientList[aleaBD][0],gradientList[aleaBD][1]);
        
        Vectf dHG = new Vectf(x - (double)x0,y - (double)y0);
    	Vectf dHD = new Vectf(x - (double)x1,y - (double)y0);
    	Vectf dBG = new Vectf(x - (double)x0,y - (double)y1);
    	Vectf dBD  = new Vectf(x - (double)x1,y - (double)y1);
        
        double contribHG = gradHG.produitScal(dHG);
    	double contribHD = gradHD.produitScal(dHD);
    	double contribBG = gradBG.produitScal(dBG);
    	double contribBD = gradBD.produitScal(dBD);
        
    	//Interpolation
    	double interpHGHD = interpolate(contribHG,contribHD,x - (double)x0);
    	double interpBGBD = interpolate(contribBG,contribBD,x - (double)x0);
    
    	double value = interpolate(interpHGHD,interpBGBD,y - (double)y0);
    	
    	return (value+1.)/2.; //On rearrange la distribution des valeurs
    }
	public static double interpolate(double a0, double a1, double t) {
	     return a0 + (a1 - a0) * fade(t);
	}
	
	public static double fade(double t) {
		return ((6*t - 15)*t + 10)*t*t*t;
	}

    public static Color[][] mapGeneration(long seed,Vecti gridSize,Vecti size){
		Random rand = new Random(seed);
		int[] permutation = new int[512];
		for (int i = 0; i < 256; i ++) {
			permutation[i] = rand.nextInt(256);
			permutation[i+56] = permutation[i];
		}		
		Color[][] colors = new Color[gridSize.x][gridSize.y];
		
		for (int i = 0; i < gridSize.x; i++) {
			for (int j = 0; j < gridSize.y; j++) {
				
				double x = ((double)i/(double)gridSize.x);
				double y = ((double)j/(double)gridSize.y);
				double n = perlin(size.x*x,size.y*y,permutation);
				
				//Eau
				if (n < 0.30) {
		        	colors[i][j] = Color.decode("#00d2ff");
				}else if (n>=0.30 && n < 0.33){
		        	colors[i][j] = Color.decode("#2edaff");
				}else if (n>=0.33 && n < 0.36){
		        	colors[i][j] = Color.decode("#50e0ff");
				}else if (n>=0.36 && n < 0.39){
		        	colors[i][j] = Color.decode("#73e6ff");
				}else if (n>=0.39 && n < 0.42){
		        	colors[i][j] = Color.decode("#9bedff");
		        //Sable
				}else if (n>=0.42 && n < 0.43){
		        	colors[i][j] = Color.decode("#f4f014");
		        }else if (n>=0.43 && n < 0.44){
		        	colors[i][j] = Color.decode("#f8ef14");
		        //Terre & Forets
		        }else if (n>=0.44 && n < 0.47){
		        	colors[i][j] = Color.decode("#00b300");
		        }else if (n>=0.47 && n < 0.54){
		        	colors[i][j] = Color.decode("#009a00");
		        }else if (n>=0.54 && n < 0.57){
		        	colors[i][j] = Color.decode("#008000");
		        }else if (n>=0.57 && n < 0.60){
		        	colors[i][j] = Color.decode("#006700");
		        }else if (n>=0.60 && n < 0.63){
		        	colors[i][j] = Color.decode("#004d00");
		        }else if (n>=0.63 && n < 0.65){
		        	colors[i][j] = Color.decode("#003400");
		        //Montagnes
		        }else if (n>=0.65 && n < 0.68){
		        	colors[i][j] = Color.decode("#808080");
		        }else if (n>=0.68 && n < 0.71){
		        	colors[i][j] = Color.decode("#737373");
		        }else if (n>=0.71 && n < 0.74){
		        	colors[i][j] = Color.decode("#666666");
		        //Neige
		        }else {
		        	colors[i][j] = Color.WHITE;
		        }
			}
		}
        return colors;
	}
    public static double aleatoire (double min, double max){
        return min + Math.random()*(max-min);
    }

    public static void main(String[] args) throws Exception {
        // Réglage
        int largeur = 600;
        int hauteur = 600;

        // Espace pour le panneau
        int panelspace = 180;

        // Côté d'une case
        int tailleCase = 100;

        // Modifie l'aspect graphique de l'écran
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        // Paramètres par défaut
        JFrame frame = new JFrame("Simulation Nuée d'Oiseaux");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(largeur + panelspace + 100, hauteur + 100);

        frame.setLocationRelativeTo(null);

        SimulationExperience simu = new SimulationExperience(largeur, hauteur, tailleCase);
        frame.add(simu);

        // Ajouter un slider pour le rayon de répulsion
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        repulsionSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double coeffRepulsion = repulsionSlider.getValue() * 0.01;
                simu.setRepulsion(coeffRepulsion);
            }
        });

        // Ajouter un slider pour le rayon d'alignement
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        alignementSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double coeffAlignement = alignementSlider.getValue() * 0.01;
                simu.setAlignement(coeffAlignement);
            }
        });

        // Ajouter un slider pour le rayon d'attraction
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        attractionSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double coeffAttraction = attractionSlider.getValue() * 0.01;
                simu.setAttraction(coeffAttraction);
            }
        });

        // Slider pour la vitesse
        JSlider vitesseSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 3);
        vitesseSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double vitesse = vitesseSlider.getValue();
                simu.setvitesse(vitesse);
            }
        });

        // Ajouter un bouton de pause
        JButton pauseButton = new JButton("Resume");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simu.setPause();
                if (simu.isPaused) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });

        // Slider pour le rayon d'attraction
        JSlider rayonAtSlider = new JSlider(JSlider.HORIZONTAL, 1, tailleCase, 100);
        rayonAtSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int r = rayonAtSlider.getValue();
                simu.setRayonAt(r);
            }
        });

        // Slider pour le rayon d'alignement
        JSlider rayonAlSlider = new JSlider(JSlider.HORIZONTAL, 1, tailleCase, 66);
        rayonAlSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int r = rayonAlSlider.getValue();
                simu.setRayonAl(r);
            }
        });

        // Slider pour le rayon de répulsion
        JSlider rayonReSlider = new JSlider(JSlider.HORIZONTAL, 1, tailleCase, 33);
        rayonReSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int r = rayonReSlider.getValue();
                simu.setRayonRe(r);
            }
        });

        // Slider pour le nombre d'oiseaux
        JSlider nbrOiseauxSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 100);
        nbrOiseauxSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int nombreOiseaux = nbrOiseauxSlider.getValue();
                simu.setNbrOiseaux(nombreOiseaux);
            }
        });

        // Ajouter les sliders et le bouton au frame
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 1));

        controlPanel.add(new JLabel("Coefficient de répulsion"));
        controlPanel.add(repulsionSlider);
        controlPanel.add(new JLabel("Coefficient d'alignement"));
        controlPanel.add(alignementSlider);
        controlPanel.add(new JLabel("Coefficient d'attraction"));
        controlPanel.add(attractionSlider);
        controlPanel.add(new JLabel("Vitesse"));
        controlPanel.add(vitesseSlider);
        controlPanel.add(new JLabel("Rayon d'attraction"));
        controlPanel.add(rayonAtSlider);
        controlPanel.add(new JLabel("Rayon d'alignement"));
        controlPanel.add(rayonAlSlider);
        controlPanel.add(new JLabel("Rayon de répulsion"));
        controlPanel.add(rayonReSlider);
        controlPanel.add(new JLabel("Nombre d'oiseaux"));
        controlPanel.add(nbrOiseauxSlider);
        controlPanel.add(pauseButton);

        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        frame.add(controlPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }
}

//Classes utilisés

class Case {
	
	int numCase;
    int xCase, yCase;
    int tailleCase;
    ArrayList<Case> caseVoisine;
    ArrayList<Oiseau> population;   

    public Case(int numCase, int xCase, int yCase, int tailleCase) {
        this.numCase = numCase;
        this.xCase = xCase;
        this.yCase = yCase;
        this.tailleCase = tailleCase;
        caseVoisine = new ArrayList<>();
        population = new ArrayList<>();
    }
    
    public void ajouterVoisin(Case voisin) {
        caseVoisine.add(voisin);
    }

}

class Oiseau {
	
    double x, y;
    double vx, vy;
    int numCase, matricule;
   
    public Oiseau(double x, double y, int numCase, int matricule) {
    	
        this.x = x;
        this.y = y;
        this.numCase = numCase;
        this.matricule = matricule;
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
    }
    
    public void deplacer(double vitesse, int largeur, int hauteur) {
        
    	// deplacer(double angleOiseau, int vitesse, int largeur, int hauteur)
    	
    	this.x += this.vx * vitesse;
        this.y += this.vy * vitesse;
        
     // mouvement avec variation
//        Random random = new Random();
//        double variation = (random.nextBoolean() ? 1 : -1) * random.nextDouble() * 10;
//        angleOiseau += variation;
//
//        double newVx = Math.cos(Math.toRadians(angleOiseau));
//        double newVy = Math.sin(Math.toRadians(angleOiseau));
//
//        vx = newVx;
//        vy = newVy;
        
     	  // Rebondir à l'intérieur de la fenêtre
        if (this.x < 0) {
        	this.vx = -this.vx;
        	this.x = 0.01;
        	}
        if (this.x >= largeur) {
        	this.vx = -this.vx;
        	this.x = largeur - 0.01;
        	}
        
        if (y < 0) {
        	this.vy = -this.vy;
        	this.y = 0.01;
        	}      	
        if (y >= hauteur) {
        	this.vy = -this.vy;
        	this.y = hauteur -0.01;
        	}
        
        
       // Traverser la fenêtre
    //    if (this.x < 0) {
    //    	this.x = largeur;
    //    }
    //    if (this.y < 0) {
    //    	this.y = hauteur;
    //    }
    //    if (this.x > largeur) {
    //    	this.x = 0;
    //    }
    //    if (this.y > hauteur) {
    //    	this.y = 0;
    //    }
    }

	public void repulsion(ArrayList<Oiseau> voisins, double CoeffRepulsion, double vitesse, int Rayon) { 
	    if (voisins.isEmpty()) {
            return;
        }
                
        double repelX = 0;
        double repelY = 0;
    
        for (Oiseau voisin : voisins) {
            // Calculer la distance entre ce boid et le voisin
            double distance = Math.sqrt(Math.pow(voisin.x - this.x, 2) + Math.pow(voisin.y - this.y, 2));

            // Si le voisin est trop proche
            if (distance < Rayon) {
                // Calculer la direction pour s'éloigner du voisin
                double directionX = this.x - voisin.x;
                double directionY = this.y - voisin.y;

                // Normaliser la direction
                double norm = Math.sqrt(directionX * directionX + directionY * directionY);
                if (norm > 0) {
                    directionX /= norm;
                    directionY /= norm;
                }

                // Ajouter à la force de répulsion
                repelX += directionX;
                repelY += directionY;
            }
        }
    
        // Appliquer la force de répulsion
        this.vx += repelX * CoeffRepulsion;
        this.vy += repelY * CoeffRepulsion;
	}

	public void alignement(ArrayList<Oiseau> voisins, double coeffAlignement, double vitesse, int RayonIn, int RayonOut) {
            if (voisins.isEmpty()) {
                return;
            }

            double avgVx = 0;
            double avgVy = 0;
            int count = 0;

            for (Oiseau voisin : voisins) {
                // Calculer la distance entre ce boid et le voisin
                double distance = Math.sqrt(Math.pow(voisin.x - this.x, 2) + Math.pow(voisin.y - this.y, 2));

                // Si le voisin est dans le 1er cercle
                if (distance < RayonIn & distance >= RayonOut){
                    avgVx += voisin.vx;
                    avgVy += voisin.vy;
                    count++;
                }
            }

            if (count > 0) {
                // Calculer la moyenne des vitesses des voisins
                avgVx /= count;
                avgVy /= count;

                // Normaliser la vitesse moyenne
                double avgSpeed = Math.sqrt(avgVx * avgVx + avgVy * avgVy);
                if (avgSpeed > 0) {
                    avgVx = (avgVx / avgSpeed) * vitesse;
                    avgVy = (avgVy / avgSpeed) * vitesse;
                }

                // Calculer la force d'alignement
                double steerVx = avgVx - this.vx;
                double steerVy = avgVy - this.vy;

                // Appliquer la force d'alignement
                this.vx += steerVx * coeffAlignement;
                this.vy += steerVy * coeffAlignement;
            }
        
	}

	public void attraction(ArrayList<Oiseau> voisins, double coeffAttraction, double vitesse, int RayonIn, int RayonOut) {
        if (voisins.isEmpty()) {
            return;
        }
    
        double avgX = 0;
        double avgY = 0;
        int count = 0;
    
        for (Oiseau voisin : voisins) {
            double Distance = Math.sqrt(Math.pow(voisin.x - this.x, 2) + Math.pow(voisin.y - this.y, 2));
            if (Distance < RayonIn & Distance >= RayonOut){
                avgX += voisin.x;
                avgY += voisin.y;
                count++;
            }
        }
    
        if (count > 0) {
            avgX /= count;
            avgY /= count;
    
            // Calculer la direction vers le centre de masse des voisins
            double directionX = avgX - this.x;
            double directionY = avgY - this.y;
    
            // Normaliser la direction
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            if (distance > 0) {
                directionX = (directionX / distance) * vitesse;
                directionY = (directionY / distance) * vitesse;
            }
    
            // Calculer la force d'attraction
            double steerX = directionX - this.vx;
            double steerY = directionY - this.vy;
    
            // Appliquer la force d'attraction
            this.vx += steerX * coeffAttraction;
            this.vy += steerY * coeffAttraction;
        }
	}

    public void normerVitesse(double vitesse){
        //règle la vitesse de l'oiseau à Vitesse, pour que tous les oiseaux aient la même vitesse.
        double norme = Math.sqrt(this.vx*this.vx + this.vy*this.vy);
        this.vx = this.vx/norme * vitesse;
        this.vy = this.vy/norme * vitesse;
    }
        
    public void dessiner(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        // Dessiner un curseur sous forme de flèche pour indiquer la direction de l'oiseau
        double cursorLength = 10; // Longueur du curseur
        double cursorAngle = Math.atan2(vy, vx); // Angle de la direction de l'oiseau

        // Coordonnées des points de la flèche
        int[] xPoints = {(int) x, (int) (x - cursorLength * Math.cos(cursorAngle - Math.PI / 8)), 
                         (int) (x - cursorLength * Math.cos(cursorAngle + Math.PI / 8))};
        int[] yPoints = {(int) y, (int) (y - cursorLength * Math.sin(cursorAngle - Math.PI / 8)), 
                         (int) (y - cursorLength * Math.sin(cursorAngle + Math.PI / 8))};

        // Dessiner la flèche
        Polygon arrow = new Polygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK); // Couleur de la flèche (noir)
        g2d.fill(arrow);
    }

    
}

// Génération carte aléatoire

class Vectf{
    public Vectf(double x_, double y_){
       this.x = x_;
       this.y = y_;
       this.norme2 = x_*x_+y_*y_;
    }
    double x;
    double y;
    double norme2;
    
    public double produitScal(Vectf Vect2) {
    	return x * Vect2.x + y * Vect2.y;
    }
}

class Vecti{
    public Vecti(int x_, int y_){
       this.x = x_;
       this.y = y_;
       this.norme2 = x_*x_+y_*y_;
    }
    int x;
    int y;
    int norme2;
    
    public int produitScal(Vecti Vect2) {
    	return x * Vect2.x + y * Vect2.y;
    }
}