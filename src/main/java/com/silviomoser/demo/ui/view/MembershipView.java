package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Membership;
import com.silviomoser.demo.data.Organization;
import com.silviomoser.demo.repository.MembershipRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static com.silviomoser.demo.utils.FormatUtils.toFirstLastName;

/**
 * Created by silvio on 10.10.18.
 */

@Component
@SpringView(name = MembershipView.VIEW_NAME)
public class MembershipView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "membership";

    I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());


    private Button addNewBtn = new Button("Neuer Eintrag", VaadinIcons.PLUS);
    private Button saveChanges = new Button("Änderungen speichern", VaadinIcons.DISC);

    private final TextField filter = new TextField();

    DateTimeField joinDateEditor = new DateTimeField(i18Helper.getMessage("membership_join"));
    DateTimeField leaveDateEditor = new DateTimeField(i18Helper.getMessage("membership_leave"));


    TabSheet tabSheet = new TabSheet();


    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private JpaRepository<Organization, Long> organizationRepository;


    @PostConstruct
    public void init() {

        Collection<Organization> allOrganizations = organizationRepository.findAll();

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, saveChanges);
        VerticalLayout mainLayout = new VerticalLayout(actions, tabSheet);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(mainLayout);


        tabSheet.setHeight(100.0f, Unit.PERCENTAGE);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        for (Organization organization : allOrganizations) {


            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);

            final Grid<Membership> grid = new Grid<>();

            grid.setHeight(800, Unit.PIXELS);
            grid.setWidth(1200, Unit.PIXELS);

            List<Membership> allMembership = membershipRepository.findByOrganization(organization);
            grid.setItems(allMembership);

            Binder<Membership> binder = grid.getEditor().getBinder();
            Binder.Binding<Membership, LocalDateTime> joinDateTimeBinding = binder.bind(joinDateEditor, Membership::getJoinDate, Membership::setJoinDate);
            Binder.Binding<Membership, LocalDateTime> leaveDateTimeBinding = binder.bind(leaveDateEditor, Membership::getLeaveDate, Membership::setLeaveDate);

            grid.addColumn(it -> toFirstLastName(it.getPerson())).setCaption(i18Helper.getMessage("membership_header_person"));
            grid.addColumn(it -> it.getJoinDate()).setEditorBinding(joinDateTimeBinding)
                    .setCaption(i18Helper.getMessage("membership_header_joined"))
                    .setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(Locale.GERMAN)));
            grid.addColumn(it -> it.getLeaveDate()).setEditorBinding(leaveDateTimeBinding)
                    .setCaption(i18Helper.getMessage("membership_header_left"))
                    .setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(Locale.GERMAN)));

            grid.addColumn(it -> it.getFunction()).setCaption(i18Helper.getMessage("membership_header_function"));

            grid.getEditor().setEnabled(true);
            grid.getEditor().setBuffered(false);

            layout.addComponent(grid);
            tabSheet.addTab(layout, organization.getName());

            saveChanges.addClickListener(e -> {
                membershipRepository.saveAll(allMembership);
            });

        }
    }
}
