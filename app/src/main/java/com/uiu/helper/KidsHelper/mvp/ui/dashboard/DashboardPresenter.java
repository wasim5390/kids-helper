package com.uiu.helper.KidsHelper.mvp.ui.dashboard;



import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.SlideItem;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.fragment.app.Fragment;

import static com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse.SlideSerialComparator;

public class DashboardPresenter implements DashboardContract.Presenter,Constant {

    private DashboardContract.View view;
    private Repository repository;
    private int count=0;
    private User user;
    private PreferenceUtil preferenceUtil;
    private List<SlideItem> slidesList;

    public DashboardPresenter(DashboardContract.View view, User user, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.user = user;
        this.preferenceUtil = preferenceUtil;
        this.slidesList = new ArrayList<>();
        this.view.setPresenter(this);
    }

    @Override
    public void addSlide(SlideItem slideItem) {
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.addAll(this.slidesList);
        slideItems.add(slideItem);
        view.onSlidesUpdated(getSortedList(slideItems));
    }

    @Override
    public void removeSlide(SlideItem slideItem) {
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.addAll(slidesList);
        slideItems.remove(slideItem);
        view.onSlidesUpdated(getSortedList(slideItems));
    }

    private void sortSlides(List<SlideItem> slidesList) {
        Collections.sort(slidesList,SlideSerialComparator);
    }

    @Override
    public void getUserSlides() {
        // view.showProgress();

        repository.getUserSlides(user.getId(), new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                slidesList.clear();

                slidesList.add(new SlideItem(SLIDE_INDEX_NOTIFICATIONS));
                for(SlideItem item: data.getSlide()){
                    if(item.getType()!=SLIDE_INDEX_HOME)
                        slidesList.add(item);
                }

                slidesList.add(createSlide("Kids Location",SLIDE_INDEX_MAP));
                slidesList.add(new SlideItem(SLIDE_INDEX_SLEEP_SETTING));
                view.onSlidesLoaded(user,getSortedList(slidesList));
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
                view.hideProgress();
            }
        });
    }



    @Override
    public void convertSlidesToFragment(List<SlideItem> slides) {
    }






    @Override
    public void start() {
        /*InviteFragment homeFragment = InviteFragment.newInstance();
        new SlideChooserPresenter(homeFragment, repository);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        view.onSlidesCreated(fragments);*/
    }

    public SlideItem createSlide(String slide,int type){
        SlideItem slideItem = new SlideItem(type);
        slideItem.setName(slide);
        slideItem.setType(type);
        slideItem.setHelperId(preferenceUtil.getAccount().getId());
        slideItem.setUser_id(user.getId());
        return slideItem;
    }

    public List<SlideItem> getSortedList(List<SlideItem> slidesList){
        /****** Removing duplicates ******/
        Set<SlideItem> hs = new HashSet();

        hs.addAll(slidesList);

        slidesList.clear();
        slidesList.addAll(hs);
        /****************************/

        /******** Updating counters ****/
        for(SlideItem slide:slidesList){
            int count=1;
            for(SlideItem slide1:slidesList){
                if(slide.getType()==slide1.getType() && slide.getId()!=slide1.getId())
                    count++;
            }
            slide.setCount(count);
        }
        /*************************/
        sortSlides(slidesList);
        return slidesList;
    }
}
