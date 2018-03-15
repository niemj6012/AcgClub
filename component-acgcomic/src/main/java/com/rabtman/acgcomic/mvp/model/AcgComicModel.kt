package com.rabtman.acgcomic.mvp

import com.rabtman.acgcomic.api.AcgComicService
import com.rabtman.acgcomic.base.constant.SystemConstant
import com.rabtman.acgcomic.mvp.model.dao.OacgComicDAO
import com.rabtman.acgcomic.mvp.model.entity.*
import com.rabtman.common.base.mvp.BaseModel
import com.rabtman.common.di.scope.ActivityScope
import com.rabtman.common.di.scope.FragmentScope
import com.rabtman.common.integration.IRepositoryManager
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * @author Rabtman
 */
@FragmentScope
class DmzjComicModel @Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), DmzjComicContract.Model {

    override fun getComicInfos(selected: String): Flowable<List<DmzjComicItem>> {
        return mRepositoryManager.obtainRetrofitService(AcgComicService::class.java)
                .getDmzjComicList(selected)
    }
}

@FragmentScope
class OacgComicModel @Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), OacgComicContract.Model {

    override fun getSearchComicInfos(keyword: String): Flowable<OacgComicPage> {
        return mRepositoryManager.obtainRetrofitService(AcgComicService::class.java)
                .searchOacgComicInfos(keyword)
    }

    override fun getComicInfos(themeId: Int, pageNo: Int): Flowable<OacgComicPage> {
        return mRepositoryManager.obtainRetrofitService(AcgComicService::class.java)
                .getOacgComicList(themeId, pageNo)
    }
}

@ActivityScope
class OacgComicDetailModel @Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), OacgComicDetailContract.Model {
    private val DAO = OacgComicDAO(mRepositoryManager.obtainRealmConfig(SystemConstant.DB_NAME))

    override fun getComicDetail(comicId: Int): Flowable<List<OacgComicEpisode>> {
        return mRepositoryManager.obtainRetrofitService(AcgComicService::class.java)
                .getOacgComicDetail(comicId)
    }

    override fun getLocalOacgComicItemById(comicInfoId: String): Flowable<OacgComicItem> {
        return DAO.getOacgComicItemById(comicInfoId)
    }

    override fun addOrDeleteLocalOacgComicItem(comicInfo: OacgComicItem, isAdd: Boolean): Completable {
        return if (isAdd) DAO.saveOacgComicItem(comicInfo) else DAO.deleteById(comicInfo.id)
    }
}

@ActivityScope
class OacgComicEpisodeDetailModel @Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), OacgComicEpisodeDetailContract.Model {

    override fun getEpisodeDetail(comicId: Int, chapterIndex: Int): Flowable<OacgComicEpisodePage> {
        return mRepositoryManager.obtainRetrofitService(AcgComicService::class.java)
                .getOacgEpisodeDetail(comicId, chapterIndex)
    }
}
