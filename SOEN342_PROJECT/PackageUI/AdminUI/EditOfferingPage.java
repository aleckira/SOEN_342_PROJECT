package PackageUI.AdminUI;


import PackageActorsAndObjects.Actor;
import PackageActorsAndObjects.Admin;
import Services.UserSession;

import javax.swing.*;

public class EditOfferingPage extends JFrame {
    public EditOfferingPage(int offeringId) {
        setTitle("Edit Offering");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //have a form with int String city, String location, String classType, int capacity, Timestamp startTime, Timestamp endTime, int instructor_id
        //We now have the choice between 5 cities...see AddOffering
        //classType is a dropdown...see AddOffering Specialty dropdown
        //capacity should be above 1
        //use Admin class's isNewOfferingUnique to check if the offering is unique
        //make sure instructor_id actually exists in instructors
        Actor user = UserSession.getCurrentUser();
        Admin a = (Admin) user;
        //a.editOffering(offeringId, city, location, classType, capacity, startTime, endTime, instructorId);
    }

}
