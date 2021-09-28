import javax.swing.*;
import java.awt.*;

public class LoadingBar extends JFrame
{
    public LoadingBar()
    {
        super("Loading...");
    }

    public void showLoadingBar()
    {
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(250,50));
        setVisible(true);
    }

    public void deleteLoadingBar()
    {
        setVisible(false);
        dispose();
    }
}
