<script setup lang="ts">
import {UserType} from "../models/user";

interface UserCardListProps {
  loading: boolean;
  userList: UserType[];
}
  const props = withDefaults(defineProps<UserCardListProps>(),{
    loading: true,
    userList: [] as UserType[]
  })
</script>

<template>
  <van-skeleton title avatar :row="3" :loading="props.loading" v-for="user in props.userList">
    <van-card
        :desc="user.profile"
        :title="`${user.username}\u3000\u3000 NO.${user.verifyCode}`"
        :thumb="user.avatarUrl"
        style="text-align: start"
    >
      <template #tags>
        <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">{{ tag }}</van-tag>
      </template>
      <template #footer>
        <van-button size="mini">联系我</van-button>
      </template>
    </van-card>
  </van-skeleton>
</template>

<style scoped>

</style>