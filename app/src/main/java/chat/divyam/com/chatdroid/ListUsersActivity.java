package chat.divyam.com.chatdroid;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import chat.divyam.com.chatdroid.Adapter.ListUserAdapter;

public class ListUsersActivity extends AppCompatActivity {

    ListView firstUser;
    Button btnCreateChat;
    int countChoice;
    ArrayList<Integer> occupantIdsList;
    QBChatDialog dialog;
    ProgressDialog mDialog;
    QBUser user;
    QBSystemMessagesManager qbSystemMessagesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        firstUser = (ListView)findViewById(R.id.firstUser);
        firstUser.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        retriveAllUser();
    }

    private void retriveAllUser() {
        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser> qbUserWithourCurrent = new ArrayList<QBUser>();
                for (QBUser user : qbUsers){
                    if (!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()))
                        qbUserWithourCurrent.add(user);
                }
                ListUserAdapter adapter = new ListUserAdapter(getBaseContext(), qbUserWithourCurrent);
                firstUser.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }
}
