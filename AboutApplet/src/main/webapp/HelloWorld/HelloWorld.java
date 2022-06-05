import java.applet.Applet;
import java.awt.Graphics;
import java.util.Date;


public class HelloWorld extends Applet 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() {
        System.out.println("initializing... ");
        repaint();
    }

    
    public void start() {
        System.out.println("start...");
        repaint();
    }
    
    public void paint(Graphics g) 
    {          
        g.drawRect(0, 0, 
		getSize().width - 1,
		getSize().height - 1);
        String msg= getParameter("texto");
        if (msg==null)
        	msg = new String("Indicar uma mensagem no parametro 'texto'!");
        msg= "("+new Date()+") " + msg;
        g.drawString(msg, 5, 55);
    }
}
