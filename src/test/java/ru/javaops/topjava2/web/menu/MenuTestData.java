package ru.javaops.topjava2.web.menu;

import ru.javaops.topjava2.model.Menu;
import ru.javaops.topjava2.web.AbstractTestData;
import ru.javaops.topjava2.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuTestData extends AbstractTestData {

    public static final MatcherFactory.Matcher<Menu> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class,  "restaurant","dishes");

    public static final Menu menu1 = new Menu(MENU1_ID + 0, LocalDate.now().minusDays(1), null, null);
    public static final Menu menu2 = new Menu(MENU1_ID + 1, LocalDate.now().minusDays(1), null, null);
    public static final Menu menu3 = new Menu(MENU1_ID + 2, LocalDate.now().minusDays(1), null, null);
    public static final Menu menu4 = new Menu(MENU1_ID + 3, LocalDate.now(), null, null);
    public static final Menu menu5 = new Menu(MENU1_ID + 4, LocalDate.now(), null, null);

    public static final List<Menu> allMenusOfRestaurant2 = List.of(menu2,menu5);


}
