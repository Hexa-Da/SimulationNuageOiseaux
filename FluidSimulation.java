import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class FluidSimulation extends JPanel {
    private static final int NX = 100; // Number of grid points along x-axis
    private static final int NY = 100; // Number of grid points along y-axis
    private static final int NT = 100; // Number of time steps
    private static final double VISCOSITY = 0.1; // Fluid viscosity
    private static final double DT = 1.0; // Time step
    private static final double DX = 1.0; // Grid spacing
    private static final double RHO0 = 1.0; // Initial density
    private static final double FORCE = 0.0001; // External force applied in x-direction

    // Lattice weights and velocities
    private static final double[] weights = {1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9, 1.0 / 9};
    private static final int[][] velocities = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {0, 0}};

    // Arrays
    private double[][][] f = new double[NY][NX][9]; // Distribution functions
    private double[][][] feq = new double[NY][NX][9]; // Equilibrium distribution functions
    private double[][] rho = new double[NY][NX]; // Density
    private double[][] ux = new double[NY][NX]; // X-component of velocity
    private double[][] uy = new double[NY][NX]; // Y-component of velocity

    public FluidSimulation() {
        // Initialization
        double[][][] f_init = new double[NY][NX][9];
        for (int k = 0; k < 9; k++) {
            for (int j = 0; j < NY; j++) {
                for (int i = 0; i < NX; i++) {
                    f_init[j][i][k] = weights[k] * RHO0;
                }
            }
        }
        f = f_init;

        // Main loop
        for (int it = 0; it < NT; it++) {
            // Collision step
            for (int k = 0; k < 9; k++) {
                for (int j = 0; j < NY; j++) {
                    for (int i = 0; i < NX; i++) {
                        double cu = velocities[k][0] * ux[j][i] + velocities[k][1] * uy[j][i];
                        feq[j][i][k] = weights[k] * rho[j][i] * (1.0 + 3.0 * cu + 9.0 / 2.0 * cu * cu - 3.0 / 2.0 * (ux[j][i] * ux[j][i] + uy[j][i] * uy[j][i]));
                    }
                }
            }
            for (int j = 0; j < NY; j++) {
                for (int i = 0; i < NX; i++) {
                    for (int k = 0; k < 9; k++) {
                        f[j][i][k] -= (1.0 / VISCOSITY) * (f[j][i][k] - feq[j][i][k]);
                    }
                }
            }

            // Streaming step
            double[][][] f_temp = new double[NY][NX][9];
            for (int k = 0; k < 9; k++) {
                for (int j = 0; j < NY; j++) {
                    for (int i = 0; i < NX; i++) {
                        int ni = (i + velocities[k][0] + NX) % NX;
                        int nj = (j + velocities[k][1] + NY) % NY;
                        f_temp[nj][ni][k] = f[j][i][k];
                    }
                }
            }
            f = f_temp;

            // Boundary conditions (periodic)
            for (int j = 0; j < NY; j++) {
                f[j][0] = f[j][NX - 2];
                f[j][NX - 1] = f[j][1];
            }
            for (int i = 0; i < NX; i++) {
                f[0][i] = f[NY - 2][i];
                f[NY - 1][i] = f[1][i];
            }

            // Macroscopic variables
            for (int j = 0; j < NY; j++) {
                for (int i = 0; i < NX; i++) {
                    double sum = 0.0;
                    for (int k = 0; k < 9; k++) {
                        sum += f[j][i][k];
                    }
                    rho[j][i] = sum;
                    ux[j][i] = (f[j][i][1] - f[j][i][3] + f[j][i][5] - f[j][i][6] - f[j][i][7] + f[j][i][8]) / rho[j][i];
                    uy[j][i] = (f[j][i][2] - f[j][i][4] + f[j][i][5] + f[j][i][6] - f[j][i][7] - f[j][i][8]) / rho[j][i];
                }
            }

            // External force
            for (int j = 0; j < NY; j++) {
                for (int i = 0; i < NX; i++) {
                    ux[j][i] += FORCE * DT;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double[][] speed = new double[NY][NX];
        for (int j = 0; j < NY; j++) {
            for (int i = 0; i < NX; i++) {
                speed[j][i] = Math.sqrt(ux[j][i] * ux[j][i] + uy[j][i] * uy[j][i]);
            }
        }

        // Draw velocity magnitude
        for (int j = 0; j < NY; j++) {
            for (int i = 0; i < NX; i++) {
                int gray = (int) (255 * speed[j][i]);
                g.setColor(new Color(gray, gray, gray));
                g.fillRect(i, j, 1, 1);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fluid Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FluidSimulation panel = new FluidSimulation();
        frame.add(panel);
        frame.setSize(NX, NY);
        frame.setVisible(true);
    }
}