import axios, {AxiosInstance} from "axios";

const isDev = process.env.NODE_ENV === 'development';

const myAxios : AxiosInstance = axios.create({
    baseURL: isDev ? 'http://localhost:8080/api/' : 'xxx',
    timeout: 100000,
});

myAxios.defaults.withCredentials = true


// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    console.log('请求', config.data)
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    console.log('响应', response.data)
    if (response?.data?.code === 40100){
        const redirectUrl = window.location.href;
        window.location.href = `/user/login?redirectUrl=${redirectUrl}`;
    }
    // 对响应数据做点什么
    return response?.data;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default myAxios;