/**
 * 队伍类别
 */
import {UserType} from "./user";

export type TeamType = {
    id: number;
    name: string;
    description: string;
    maxNum: number;
    expireTime?: Date;
    userId?:number;
    status: number;
    password?: string;
    createTime: Date;
    updateTime: Date;
    createUser?: UserType;
    hasJoin?:Boolean;
    hasJoinNum?: number;
};