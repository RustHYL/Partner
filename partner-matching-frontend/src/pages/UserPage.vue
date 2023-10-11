<template>
  <div style="text-align: start" v-if="user">
    <van-cell title="昵称" is-link :value="user.username" @click="toEdit('username','昵称',user.username)" />
    <van-cell title="用户账号" :value="user.userAccount" />
    <van-cell title="头像" is-link to="user/edit">
      <img style="height: 48px; width: 48px" :src="user.avatarUrl" />
    </van-cell>
    <van-cell title="性别" is-link :value="user.gender" @click="toEdit('gender','性别',user.gender)" />
    <van-cell title="电话" is-link :value="user.phone" @click="toEdit('phone','电话',user.phone)" />
    <van-cell title="邮箱" is-link :value="user.email" @click="toEdit('email','邮箱',user.email)" />
    <van-cell title="校验编号" :value="user.verifyCode" />
    <van-cell title="注册时间" :value="user.createTime.toISOString()" />
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {showFailToast} from "vant";
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
  const res = await myAxios.get('/user/current')
  if (res.code === 0){
    user.value = res.data;
  }else {
    showFailToast('获取当前用户失败');
  }
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