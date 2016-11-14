package androidkejar.noteapp.database;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Dodi Rivaldi on 13/11/2016.
 */

public class RealmDB {

    private Realm realm;
    private Context context;

    public RealmDB(Context context) {
        this.context = context;
        realm = Realm.getInstance(context);
    }

    public void setMigration(RealmMigration migration) {
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .schemaVersion(0)
                .name("example.realm")
                .migration(migration)
                .build();

        realm = Realm.getInstance(config);
    }

    public Realm getRealm(){
        return realm;
    }

    @SuppressWarnings("unchecked")
    public <T extends RealmObject> T getById(Class<? extends RealmObject> cls, int id) {
        return (T) this.getRealm().where(cls.asSubclass(RealmObject.class)).equalTo("id", id).findFirst();
    }

    public RealmResults<? extends RealmObject> getAllData(Class<? extends RealmObject> cls) {
        return this.getRealm().where(cls.asSubclass(RealmObject.class)).findAll();
    }

    public void add(RealmObject object) {
        this.getRealm().beginTransaction();
        this.getRealm().copyToRealm(object);
        this.getRealm().commitTransaction();
    }

    public void add(List<RealmObject> listObject) {
        this.getRealm().beginTransaction();
        this.getRealm().copyToRealm(listObject);
        this.getRealm().commitTransaction();
    }

    public void delete(Class<? extends RealmObject> cls, int id) {
        RealmResults results = this.getRealm().where(cls.asSubclass(RealmObject.class)).equalTo("id", id).findAll();
        this.getRealm().beginTransaction();
        results.clear();
        this.getRealm().commitTransaction();
    }

    public void update(RealmObject object){
        this.getRealm().beginTransaction();
        this.getRealm().copyToRealmOrUpdate(object);
        this.getRealm().commitTransaction();
    }
}
