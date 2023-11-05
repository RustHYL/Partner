/**
 * 用户类别
 */

export type UserType = {
    id: number;
    username: string;
    userAccount: string;
    avatarUrl: string;
    gender: number;
    phone: string;
    email: string;
    profile?: string;
    userStatus: number;
    userRole: number;
    verifyCode: string;
    createTime: Date;
    tags: string[];
};