import { request } from '@/utils/http'

export const getDataTypeList = data => request({
  method: 'get',
  url: '/app/topicType/getAppTopicTypeList',
  data
})

export const getChapterInfo = data => request({
  method: 'get',
  url: '/app/dataCenter/getUserDatePractice',
  data,
  isLoading: true
})

export const getBankOfTypeList = data => request({
  method: 'get',
  url: '/app/topicBank/getTopicBankList',
  data,
  isLoading: true
})

export const getSubjectOfTypelist = data => request({
  method: 'get',
  url: '/app/topicLibrary/getAppTopicLibraryList',
  data
})

export const getTopicList = data => request({
  method: 'get',
  url: '/app/topicMatter/getTopicMatterList',
  data
})

export const saveAnswer = data => request({
  method: 'post',
  url: '/app/topicAnswer/saveTopicAnswer',
  data,
  isLoading: true
})

export const saveMatter = data => request({
  method: 'get',
  url: '/app/topicMatter/saveAnswerMatter',
  data
})

export const getSheetList = data => request({
  url: '/app/topicAnswer/getMyTopicAnswerInfo',
  method: 'get',
  data
})

export const getMatterById = data => request({
  url: '/app/topicMatter/getTopicMatterById',
  method: 'get',
  data
})

export const login = data => request({
  method: 'post',
  url: '/app/Login/quickLogin',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

export const login_p = data => request({
  url: '/sqx_fast/app/Login/loginApp',
  method: 'post',
  data
})

export const register = data => request({
  method: 'post',
  url: '/app/Login/registApp',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

export const sendMsg = phone => request({
  method: 'get',
  url: `/app/Login/sendMsg/${phone}/1`,
})


export const createExam = data => request({
  url: '/app/topicExam/saveTopicExam',
  method: 'post',
  data,
  isLoading: true
})

export const getExamList = data => request({
  url: '/app/topicExam/getTopicExamList',
  method: 'get',
  data
})

export const getExamTopicList = data => request({
  url: '/app/topicExamMatter/getTopicExamMatterList',
  method: 'get',
  data,
  isLoading: true
})

export const getStatisticsByBankId = data => request({
  url: '/app/topicExam/getBankInfo',
  method: 'get',
  data
})

export const getVipPayInfoList = data => request({
  url: '/app/vipDetails/getVipDetailsList',
  method: 'get',
  data,
  isLoading: true
})

export const buyVip = data => request({
  url: '/app/vipDetails/userBuyVip',
  method: 'post',
  data,
  header: {
    'content-type': 'application/x-www-form-urlencoded'
  }
})

export const submitExamAnswerCard = (data) => request({
  url: '/app/topicExamMatter/saveTopicExamMatter',
  method: 'post',
  data
})

export const getHomeBannerList = data => request({
  url: '/app/banner/selectBannerList',
  method: 'get',
  data
})

export const getMyAnswerRecords = data => request({
  url: '/app/topicAnswer/getTopicAnswerList',
  method: 'get',
  data
})

export const getMyAnswerList = data => request({
  url: '/app/topicLibrary/getMyAnswerList',
  method: 'get',
  data
})

export const getTopicContestList = data => request({
  url: '/app/topicContest/getAppTopicContestList',
  method: "get",
  data
})

export const getContestDetail = data => request({
  url: '/app/topicContest/getAppTopicContestInfo',
  method: 'get',
  data
})

export const getSubMatterList = data => request({
  url: '/app/topicContestMatter/getTopicContestMatterList',
  method: 'get',
  data
})

export const recordVideoStart = data => request({
  url: '/app/topicCourseMinute/saveCourseMinute',
  method: 'get',
  data
})

export const getTopicLibrary = data => request({
  url: '/app/topicLibrary/getAnswerRecordList',
  method: 'get',
  data
})

export const getMyData = data => request({
  url: '/app/dataCenter/getMyTopicInfo',
  method: 'get',
  data
})

export const signExamRace = ({ contestId }) => request({
  url: `/app/topicContestSign/userSign?contestId=${contestId}`,
  method: 'post'
})

export const getExamStatus = data => request({
  url: '/app/topicContestAnswer/getTopicContestAnswerByContestId',
  method: 'get',
  data
})

export const getTopicMatterAnswerList = data => request({
  url: '/app/topicAnswer/getTopicMatterAnswerList',
  method: 'get',
  data
})

export const getRandomTopicMatterList = data => request({
  url: '/app/topicMatter/getRandomTopicMatterList',
  method: 'get',
  data
})

export const reminderOrder = data => request({
  url: '/app/message/reminderOrder',
  method: 'get',
  data
})

export const save = data => request({
  url: '/app/topicContestSubjectRecord/save',
  method: 'post',
  data
})
export const saveTopicContestAnswer = data => request({
  url: '/app/topicContestAnswer/saveTopicContestAnswer',
  method: 'post',
  data
})

export * from './modules/user'
export * from './modules/good'
export * from './modules/address'
export * from './modules/order'