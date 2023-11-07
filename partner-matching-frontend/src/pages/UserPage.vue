<template>
  <div style="text-align: start" v-if="user">
    <van-cell title="当前用户" :value="user?.username" />
    <van-cell title="我的信息" is-link to="/user/update" />
    <van-cell title="我创建的队伍" is-link to="/user/team/create" />
    <van-cell title="我加入的队伍" is-link to="/user/team/join" />
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {getCurrentUser} from "../services/user.ts";
//
// const user = {
//   id: 1,
//   username: 'hapi',
//   userAccount: '1257635375',
//   avatarUrl: 'https://img1.baidu.com/it/u=3605832793,3252065221&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=1422',
//   gender: 0,
//   phone: 17815700343,
//   email: '1257635375@qq.com',
//   userRole: 1,
//   verifyCode: '66645',
//   createTime: new Date(),
// }

const user = ref()

onMounted(async ()=>{
  user.value = await getCurrentUser();
})

const router = useRouter();
const toEdit = (editKey: string,editName: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue,
    }
  })
}


</script>

<style scoped>

</style>