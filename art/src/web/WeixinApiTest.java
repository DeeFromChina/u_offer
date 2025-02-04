import org.sword.wechat4j.event.EventType;
import org.sword.wechat4j.menu.Menu;
import org.sword.wechat4j.menu.MenuButton;
import org.sword.wechat4j.menu.MenuManager;

import java.util.ArrayList;

/**
 * Created by drol on 2016/11/29.
 */
public class WeixinApiTest {

    public static void main(String[] args) {

        try {
            MenuButton menuButton = new MenuButton();
            menuButton.setName("菜单1");
            menuButton.setType(EventType.click);
            menuButton.setKey("menu1");
            MenuButton menuButton2 = new MenuButton();
            menuButton2.setName("菜单2");
            menuButton2.setKey("菜单1");
            menuButton2.setType(EventType.click);
            MenuButton menuButton3 = new MenuButton();
            menuButton3.setName("菜单3");
            menuButton3.setKey("菜单1");
            menuButton3.setType(EventType.click);

            MenuButton subButton = new MenuButton();
            subButton.setName("子菜单1");
            subButton.setType(EventType.click);
            subButton.setKey("subMenu1");
            ArrayList<MenuButton> subButtons = new ArrayList<MenuButton>();
            subButtons.add(subButton);
            menuButton.setSubButton(subButtons);

            ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
            menuButtons.add(menuButton);
            menuButtons.add(menuButton2);
            menuButtons.add(menuButton3);

            Menu menu = new Menu();
            menu.setButton(menuButtons);

            MenuManager menuManager = new MenuManager();
            menuManager.create(menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
