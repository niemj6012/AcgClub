package com.rabtman.acgschedule.mvp.model.dao;

import com.rabtman.acgschedule.mvp.model.entity.ScheduleCollection;
import com.rabtman.acgschedule.mvp.model.entity.ScheduleHistory;
import com.rabtman.common.utils.RxRealmUtils;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import java.util.List;

/**
 * @author Rabtman
 */
public class ScheduleDAO {

  private RealmConfiguration realmConfiguration;

  public ScheduleDAO(RealmConfiguration realmConfiguration) {
    this.realmConfiguration = realmConfiguration;
  }

  public Completable addScheduleCollection(final ScheduleCollection item) {
    return RxRealmUtils.exec(realmConfiguration, new Consumer<Realm>() {
      @Override
      public void accept(Realm realm) throws Exception {
        realm.executeTransactionAsync(new Transaction() {
          @Override
          public void execute(Realm r) {
            r.copyToRealmOrUpdate(item);
          }
        });
      }
    });
  }

  public Completable addScheduleHistory(final ScheduleHistory item) {
    return RxRealmUtils.exec(realmConfiguration, new Consumer<Realm>() {
      @Override
      public void accept(Realm realm) throws Exception {
        realm.executeTransactionAsync(new Transaction() {
          @Override
          public void execute(Realm r) {
            r.copyToRealmOrUpdate(item);
          }
        });
      }
    });
  }

  public Completable deleteByUrl(final String url) {
    return RxRealmUtils.exec(realmConfiguration, new Consumer<Realm>() {
      @Override
      public void accept(Realm realm) throws Exception {
        realm.executeTransactionAsync(new Transaction() {
          @Override
          public void execute(Realm r) {
            r.where(ScheduleCollection.class)
                .equalTo("scheduleUrl", url)
                .findAll()
                .deleteAllFromRealm();
          }
        });
      }
    });
  }

  public Flowable<ScheduleCollection> getScheduleCollectionByUrl(String url) {
    try (final Realm realm = Realm.getInstance(realmConfiguration)) {
      ScheduleCollection queryResult = realm.where(ScheduleCollection.class)
          .equalTo("scheduleUrl", url)
          .findFirst();
      if (queryResult != null) {
        return Flowable.just(realm.copyFromRealm(queryResult));
      } else {
        return Flowable.empty();
      }
    }
  }

  public Flowable<ScheduleHistory> getScheduleHistoryByUrl(String url) {
    try (final Realm realm = Realm.getInstance(realmConfiguration)) {
      ScheduleHistory queryResult = realm.where(ScheduleHistory.class)
          .equalTo("scheduleUrl", url)
          .findFirst();
      return Flowable.just(
          queryResult == null ? new ScheduleHistory(url, -1) : realm.copyFromRealm(queryResult)
      );
    }
  }

  public Flowable<List<ScheduleCollection>> getScheduleCollections() {
    try (final Realm realm = Realm.getInstance(realmConfiguration)) {
      RealmResults<ScheduleCollection> queryResult = realm.where(ScheduleCollection.class)
          .findAll();
      if (queryResult != null) {
        return Flowable.just(realm.copyFromRealm(queryResult));
      } else {
        return Flowable.empty();
      }
    }
  }
}