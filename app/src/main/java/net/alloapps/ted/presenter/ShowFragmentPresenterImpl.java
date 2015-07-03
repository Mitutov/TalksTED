package net.alloapps.ted.presenter;

import net.alloapps.ted.view.IShowFragmentView;

import javax.inject.Inject;

public class ShowFragmentPresenterImpl implements IShowFragmentPresenter {

    private IShowFragmentView view;

    @Inject
    public ShowFragmentPresenterImpl(){
    }

    @Override
    public void init(IShowFragmentView view) {
        this.view = view;
    }
}
