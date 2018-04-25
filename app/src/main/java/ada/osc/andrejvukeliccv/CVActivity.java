package ada.osc.andrejvukeliccv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CVActivity extends AppCompatActivity {

    private List<KeyAchivement> achivements = new ArrayList<>();

    @BindView(R.id.facebookProfile)
    TextView facebookContact;
    @BindView(R.id.twitterProfile)
    TextView twitterContact;
    @BindView(R.id.githubProfile)
    TextView githubContact;
    @BindView(R.id.linkedinProfile)
    TextView linkedinProfileContact;
    @BindView(R.id.phoneNumber)
    TextView phoneNumberContact;
    @BindView(R.id.email)
    TextView email;

    ListView achivementListView;
    KeyAchivementsAdapter achivementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv);
        createAchivement(R.drawable.education_icon, "Education", createEducationText());
        createAchivement(R.drawable.experience_icon, "Experience", createExperienceText());
        createAchivement(R.drawable.pc_icon, "Digital skills", createPcSkillsText());
        createAchivement(R.drawable.hobbies_icon, "Hobbies", createHobbiesText());

        achivementListView = findViewById(R.id.key_achivements_list);

        achivementsAdapter = new KeyAchivementsAdapter(this, achivements);
        achivementListView.setAdapter(achivementsAdapter);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.email)
    public void sendEmail(){
        
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("avukelic031@gmail.com") +
                "?subject=" + Uri.encode("Do you want to work for us?");
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail..."));
    }
    @OnClick(R.id.phoneNumber)
    public void makeCall() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumberContact.getText().toString())));
    }
    @OnClick(R.id.facebookProfile)
    public void checkFBProfile(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/an.vuk")));
    }
    @OnClick(R.id.twitterProfile)
    public void checkTwitterProfile(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/vukelic_andrej")));
    }
    @OnClick(R.id.githubProfile)
    public void checkGithubProfile(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/avukelic")));
    }
    @OnClick(R.id.linkedinProfile)
    public void checkLinkedinProfile(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://hr.linkedin.com/in/anvukelic")));
    }


    private void createAchivement(int imageId, String title, String text) {
        KeyAchivement achivement = new KeyAchivement();
        achivement.setImageId(imageId);
        achivement.setName(title);
        achivement.setText(text);
        achivements.add(achivement);
    }

    private String createEducationText() {
        StringBuilder sb = new StringBuilder();
        sb.append("10/2016-now\nMaster of Economics\n");
        sb.append("Faculty of Economics in Osijek, Osijek (Croatia)\n" +
                "▪ General: English\n" +
                "▪ Occupational: Web application development, M-marketing, Multimedia marketing\n\n");
        sb.append("10/2013-06/2016\nBachelor of Economics\n");
        sb.append("Faculty of Economics in Osijek, Osijek (Croatia)\n" +
                "▪ General: English, Introduction in finance\n" +
                "▪ Occupational: Web application development, E-marketing, E-commerce, M-marketing\n\n");
        sb.append("09/2009-06/2013\n" + "Natural Science and Mathematics High School, Osijek (Croatia) \n");


        return sb.toString();
    }

    private String createExperienceText() {
        StringBuilder sb = new StringBuilder();
        sb.append("06/2016-09/2016\nSeasonal worker\n");
        sb.append("Thon hotels, Kristiansand (Norway)\n" +
                "▪ Housekeeping and helping guests with any requests they have\n\n");
        sb.append("11/2015-06/2016\nStudent job\n");
        sb.append("Spurs j.d.o.o. - Caffe bar Lloret, Osijek (Croatia) \n");


        return sb.toString();
    }

    private String createHobbiesText() {
        StringBuilder sb = new StringBuilder();
        sb.append("I am very passionate about traveling and discovering new cultures (food, drinks, mores).\n" +
                "Also I am big beer lover, want to learn more about brewing my own beer(have tried once - nobody didn't die)");


        return sb.toString();
    }

    private String createPcSkillsText() {
        StringBuilder sb = new StringBuilder();
        sb.append("▪ Good knowledge of Microsoft Office™ tools (Word™, Excel™ and PowerPoint™)\n" +
                "▪ Familiar of HTML and CSS\n" +
                "▪ Good understanding of OOP principles\n" +
                "▪ Basic knowledge of Java programming language");


        return sb.toString();
    }

}
