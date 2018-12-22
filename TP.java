
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class TP extends JFrame {
    
    private JMenuBar menubar = new JMenuBar();
    private JButton bt = new JButton("Open");
    private JFileChooser fc = new JFileChooser("./");
    private int[] imagePixels;
    private DrawingPanel dp = new DrawingPanel();
    private Timer zaman = new Timer(1000, new TimerListener());
    private FileInputStream fis;
    private int rn,w,h,pixelNo,counter;
    private boolean drawImage = false;
    private String fileType;
    TP() {
            this.setSize(500, 500);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.add(menubar);
            this.add(bt,BorderLayout.SOUTH);
            this.add(dp);
            bt.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                     fc.showOpenDialog(null);
                                     File sf = fc.getSelectedFile();
                                     getFile(sf.getName());
                                }
            });
            this.setVisible(true);
    }
    private void P1() {
            w = readNumber();
            skipWhitespace();
            h = readNumber();
            skipWhitespace();
            pixelNo = w*h;
            imagePixels = new int[pixelNo];
            for(int i =0; i < imagePixels.length; i++) {
                    imagePixels[i] = readNumber();
                    skipWhitespace();
            }
            paintImage();      
    } 
    
    private void P2() {
            w = readNumber();
            skipWhitespace();
            h = readNumber();
            skipWhitespace();
            readNumber();
            skipWhitespace();
            pixelNo = w*h;
            imagePixels = new int[pixelNo];
            for(int i=0; i < imagePixels.length; i++) {
                imagePixels[i] = readNumber();
                skipWhitespace();    
            }
            paintImage();    
    }
    
    private void P3() {
       w = readNumber();
       skipWhitespace();
       h = readNumber();
       skipWhitespace();
       readNumber();
       skipWhitespace();
       pixelNo = (w*h)*3;
       imagePixels = new int[pixelNo];
       for (int i = 0; i < imagePixels.length; i++) {
           imagePixels[i] = readNumber();
           skipWhitespace();
       }
       paintImage();
   }
   private void P4() {
       w = readNumber();
       skipWhitespace();
       h = readNumber();
       skipWhitespace();
       pixelNo = w*h;
       imagePixels = new int[pixelNo];
       int bitcounter = 128;
       int indexct = 0;
       int f = ((w/8)+1)*h;
       byte[] pixelColor = new byte[f];
       pixelColor[0] = (byte) ' ';
       for (int i = 1; i < f; i++) {
           try {
               pixelColor[i] = (byte) fis.read();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       for (int i = 0; i < f; i++) {
           for (int j = 0; j < 8; j++) {
               imagePixels[indexct] = pixelColor[i] & bitcounter;
               bitcounter/=2;
               indexct++;
               if (indexct % w == 0)
                   break;
           }
           bitcounter = 128;
       }
       paintImage();
   }
   private void P5() {
       w = readNumber();
       skipWhitespace();
       h = readNumber();
       skipWhitespace();
       readNumber();
       pixelNo = (w*h)*3;
       imagePixels = new int[pixelNo];
       for (int i = 0; i < imagePixels.length; i++) {
           try {
               imagePixels[i] = fis.read();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       paintImage();
   }
   private void P6() {
       w = readNumber();
       skipWhitespace();
       h = readNumber();
       skipWhitespace();
       readNumber();
       pixelNo = (w*h)*3;
       imagePixels = new int[pixelNo];
       for (int i = 0; i < imagePixels.length; i++) {
           try {
               imagePixels[i] = fis.read();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       paintImage();
   }
    
    public void getFile(String fn){
        try {
                fis = new FileInputStream(fn);
        }   catch (FileNotFoundException e){
                    e.printStackTrace();
        }
        fileType = getMagicNumber();
        skipWhitespace();
        if(fileType.equals("P1")) P1(); 
        else if(fileType.equals("P2")) P2(); 
        else if(fileType.equals("P3")) P3(); 
        else if(fileType.equals("P4")) P4(); 
        else if(fileType.equals("P5")) P5(); 
        else if(fileType.equals("P6")) P6(); 
    }
    
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(drawImage) {
                dp.repaint();
                counter++;
                if (counter >= 5)
                    zaman.stop();
            }
        }
    }
    
    public void paintImage() {
            counter = 0;
            drawImage = true;
            zaman.start();
    }

    class DrawingPanel extends JPanel {
                        public void paintComponent(Graphics g) {
                                        super.paintComponent(g);
                                        int n=0;
                                        if(drawImage) {
                                                int form = h/5*counter;
                                                System.out.println(form);
                                                for(int y=0; y < form; y++){
                                                    for (int x=0; x < w; x++) {
                                                        int color = imagePixels[y*w+x];
                                                        if(fileType.equals("P1"))
                                                            g.setColor(color == 0 ? Color.WHITE : Color.BLACK);
                                                        else if(fileType.equals("P2"))
                                                            g.setColor(new Color(color,color,color));
                                                        else if(fileType.equals("P3"))
                                                            g.setColor(new Color(imagePixels[n], imagePixels[n +1], imagePixels[n +2]));
                                                        else if(fileType.equals("P4"))
                                                            g.setColor(color == 0 ? Color.WHITE : Color.BLACK);
                                                        else if(fileType.equals("P5"))
                                                            g.setColor(new Color(color,color,color));
                                                        else if(fileType.equals("P6"))
                                                            g.setColor(new Color(imagePixels[n], imagePixels[n + 1], imagePixels[n + 2]));
                                                        g.fillRect(x, y, 1, 1);
                                                        n+=3;
                                                    }
                                                }
                                        }
                        }
        
    }
  
    private String getMagicNumber() {
        byte [] magicNum = new byte[2];
        try {
            fis.read(magicNum);
        } catch (IOException e){
            e.printStackTrace();
        }
        return new String(magicNum);
    }
    
    private void skipWhitespace() {
        try {
            rn = fis.read();
            while (Character.isWhitespace(rn))
                    rn = fis.read();
        } catch (IOException e){
                e.printStackTrace();
        }
    }
    
    private int readNumber() {
        String wstr = "";
        try {
            while (!Character.isWhitespace(rn)){
                    wstr = wstr + (rn - '0');
                    rn = fis.read();
            }
        }   catch (IOException e2){}
            return Integer.parseInt(wstr);
    }
    
    
    
    
    public static void main(String[] args) {
            new TP();
    }
    
}
