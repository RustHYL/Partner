<script setup lang="ts">
import {TeamType} from "../models/team";
import {teamStatusEnum} from "../constants/team.ts";
import pfp from "../assets/pfp.png";
import myAxios from "../plugins/myAxios.ts";
import {showFailToast, showSuccessToast} from "vant";
import {getCurrentUser} from "../services/user.ts";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

interface TeamCardListProps {
  teamList: TeamType[];
}

const props = withDefaults(defineProps<TeamCardListProps>(),{
  teamList: [] as TeamType[]
});

const currentUser = ref();

const router = useRouter();

onMounted(async () => {
  currentUser.value = await getCurrentUser();
})

/**
 * 加入队伍
 * @param id
 */
const doJoinTeam = async (id: number) => {
  const res = await myAxios.post('team/join', {
    teamId: id
  });
  if (res.code === 0){
    showSuccessToast("加入成功")
  } else {
    showFailToast('加入失败' + (res.description ? `，${res.description}` : ''))
  }
}

const doUpdateTeam = async (id: number) => {
  router.push({
    path: 'team/update',
    query: {
      id,
    }
  })
}

/**
 * 退出队伍
 * @param id
 */
const doQuitTeam = async (id: number) => {
  const res = await myAxios.post('team/quit', {
    teamId: id
  });
  if (res.code === 0){
    showSuccessToast("操作成功")
  } else {
    showFailToast('操作失败' + (res.description ? `，${res.description}` : ''))
  }
}

/**
 * 解散队伍
 * @param id
 */
const doDeleteTeam = async (id: number) => {
  const res = await myAxios.post('team/delete', {
    id,
  });
  if (res.code === 0){
    showSuccessToast("操作成功")
  } else {
    showFailToast('操作失败' + (res.description ? `，${res.description}` : ''))
  }
}

</script>

<template>
  <div id="teamCardList">
    <van-card
        v-for="team in props.teamList"
        :desc="team.description"
        :title="`${team.name}`"
        :thumb="pfp"
        style="text-align: start"
    >
      <template #tags>
        <van-tag plain type="primary" style="margin-right: 8px; margin-top: 8px">{{ teamStatusEnum[team.status] }}</van-tag>
      </template>
      <template #bottom>
        <div>
          {{ '最大人数: ' + team.maxNum }}
        </div>
        <div v-if="team.expireTime">
          {{ '过期时间: ' + team.expireTime }}
        </div>
        <div>
          {{ '创建时间: ' + team.createTime }}
        </div>
      </template>

      <template #footer>
        <van-button v-if="team.userId !== currentUser?.id && !team.hasJoin" type="primary" plain @click="doJoinTeam(team.id)" size="small">加入队伍</van-button>
        <van-button v-if="team.userId === currentUser?.id" plain @click="doUpdateTeam(team.id)" size="small">更新队伍</van-button>
        <van-button plain v-if="team.userId !== currentUser?.id && team.hasJoin" @click="doQuitTeam(team.id)" size="small">退出队伍</van-button>
        <van-button v-if="team.userId === currentUser?.id" type="danger" plain @click="doDeleteTeam(team.id)" size="small">解散队伍</van-button>
      </template>
    </van-card>
  </div>
</template>

<style scoped>
#teamCardList :deep(.van-image__img) {
  height: 136px;
  object-fit: unset;
}
</style>