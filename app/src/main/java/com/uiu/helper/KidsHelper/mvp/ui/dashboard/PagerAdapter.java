package com.uiu.helper.KidsHelper.mvp.ui.dashboard;


import com.uiu.helper.CompanionApp;
import com.uiu.helper.KidsHelper.mvp.model.SleepSetting;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list.NotificationsListFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.notifications_list.NotificationsListPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.SafePlacesFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.safe_places.SafePlacesPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.sleep.SleepFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.sleep.SleepPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.KidLocationFragment;
import com.uiu.helper.KidsHelper.mvp.ui.tracker.KidLocationPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.applications.FavoriteAppFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.applications.FavoriteAppsPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.contacts.FavoritePeopleFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.contacts.FavoritePeoplePresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.links.FavoriteLinksFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.links.FavoriteLinksPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.reminder.ReminderFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.reminder.ReminderPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.slides.sos.SosFragment;
import com.uiu.helper.KidsHelper.mvp.ui.slides.sos.SosPresenter;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_NOTIFICATIONS;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_SAFE_PLACES;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_FAV_APP;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_FAV_LINKS;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_FAV_PEOPLE;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_MAP;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_REMINDERS;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_SLEEP_SETTING;
import static com.uiu.helper.KidsHelper.mvp.Constant.SLIDE_INDEX_SOS;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<SlideItem> list;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Repository repository;
    private PreferenceUtil preferenceUtil;
    private User user;

    public PagerAdapter(FragmentManager fm, List<SlideItem> fragments, User userEntity, Repository repository, PreferenceUtil preferenceUtil) {
        super(fm);
        this.list = fragments;
        this.user = userEntity;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        createFragments(list);
        notifyDataSetChanged();
    }

    public void setSlides(List<SlideItem> fragmets){
        this.list.clear();
        notifyDataSetChanged();
        this.list.addAll(fragmets);
        createFragments(list);
        notifyDataSetChanged();
    }

    private void createFragments(List<SlideItem> slides){
        fragmentList.clear();
        for(SlideItem slideItem:slides)
        fragmentList.add(createFragmentsFromSlide(slideItem));
    }


   /* public Object instantiateItem(ViewGroup container, int position)
    {
        Object ret = super.instantiateItem(container, position);
        list.remove(position);
        list.add(position,(Fragment)ret);
        return ret;
    }*/

   public int getSlideIndex(int slideType){
       for(int i=list.size()-1;i>=0;i--){
           if(list.get(i).getType()==slideType)
               return i;
       }
       return 0;
   }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
      ///  SlideItem fragment = list.get(position);
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private Fragment createFragmentsFromSlide(SlideItem slideItem){
        User helper=  preferenceUtil.getAccount();
        if(slideItem!=null)
            switch (slideItem.getType()){
                case SLIDE_INDEX_SLEEP_SETTING:
                    SleepFragment sleepFragment = SleepFragment.newInstance(user.getId());
                    new SleepPresenter(sleepFragment,user.getId(), PreferenceUtil.getInstance(CompanionApp.getInstance()), repository);
                    return sleepFragment;
                case SLIDE_INDEX_NOTIFICATIONS:
                    NotificationsListFragment notificationsListFragment = NotificationsListFragment.newInstance();
                    new NotificationsListPresenter(notificationsListFragment, PreferenceUtil.getInstance(CompanionApp.getInstance()),user, repository);
                    return notificationsListFragment;
                case SLIDE_INDEX_FAV_PEOPLE:
                    FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
                    new FavoritePeoplePresenter(favoritePeopleFragment,slideItem, PreferenceUtil.getInstance(CompanionApp.getInstance()),user, repository);
                    return favoritePeopleFragment;

                case SLIDE_INDEX_FAV_APP:
                    FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance();
                    new FavoriteAppsPresenter(appsFragment, slideItem,helper.getId() ,user, repository);
                    return appsFragment;

                case SLIDE_INDEX_FAV_LINKS:
                    FavoriteLinksFragment linksFragment = FavoriteLinksFragment.newInstance();
                    new FavoriteLinksPresenter(linksFragment,slideItem, preferenceUtil,user, repository);
                    return linksFragment;

                case SLIDE_INDEX_SOS:
                    SosFragment sosFragment = SosFragment.newInstance();
                    new SosPresenter(sosFragment,slideItem, preferenceUtil,user, repository);
                    return sosFragment;
                case SLIDE_INDEX_REMINDERS:
                    ReminderFragment reminderFragment = ReminderFragment.newInstance();
                    new ReminderPresenter(reminderFragment,slideItem, preferenceUtil,user, repository);
                    return reminderFragment;

                case SLIDE_INDEX_SAFE_PLACES:
                    SafePlacesFragment directionFragment = SafePlacesFragment.newInstance();
                    SafePlacesPresenter safePlacesPresenter = new SafePlacesPresenter(directionFragment,slideItem,preferenceUtil ,user, repository);
                    return directionFragment;
                case SLIDE_INDEX_MAP:
                    KidLocationFragment fragment = KidLocationFragment.newInstance();
                    KidLocationPresenter presenter = new KidLocationPresenter(fragment, user, repository);
                    return fragment;

            }
        return null;
    }
}
