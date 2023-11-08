import Index from "../pages/Index.vue";
import Team from "../pages/TeamPage.vue";
import UserPage from "../pages/UserPage.vue";
import SearchPage from "../pages/SearchPage.vue";
import UserEditPage from "../pages/UserEditPage.vue";
import SearchResultPage from "../pages/SearchResultPage.vue";
import UserLoginPage from "../pages/UserLoginPage.vue";
import TeamAddPage from "../pages/TeamAddPage.vue";
import TeamUpdatePage from "../pages/TeamUpdatePage.vue";
import UserUpdatePage from "../pages/UserUpdatePage.vue";
import UserTeamJoinPage from "../pages/UserTeamJoinPage.vue";
import UserTeamCreatePage from "../pages/UserTeamCreatePage.vue";



const routes = [
    { path: '/', component: Index },
    { path: '/team', component: Team, title: '队伍' },
    { path: '/user', component: UserPage, title: '个人' },
    { path: '/search', component: SearchPage, title: '搜索用户' },
    { path: '/user/login', component: UserLoginPage, title: '登录'},
    { path: '/user/list', component: SearchResultPage },
    { path: '/user/edit', component: UserEditPage, title: '编辑信息' },
    { path: '/user/update', component: UserUpdatePage, title: '用户信息' },
    { path: '/user/team/join', component: UserTeamJoinPage, title: '我加入的队伍' },
    { path: '/user/team/create', component: UserTeamCreatePage, title: '我创建的队伍' },
    { path: '/team/add', component: TeamAddPage, title: '创建队伍' },
    { path: '/team/update', component: TeamUpdatePage, title: '更新队伍' },
]

export default routes;