// 将 axios 的请求封装到一个独立的文件中，并通过 Vue 界面来调用，可以有效避免非同步（异步）操作带来的问题。
import axios from 'axios'
import { ElMessage } from 'element-plus'

const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
    console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`) //模板字符串用 反引号（backticks） 包裹, 可以在字符串中直接嵌入变量或表达式，使用 ${表达式} 的形式
    ElMessage.warning(message)
}

const defaultError = (err) => {
    console.error(err)
    ElMessage.warning('发生了一些错误，请联系管理员')
}

function accessHeader() {
    const token = takeAccessToken();

    return token ? {'Authorization' : `Bearer ${takeAccessToken()}`} : {}
}

function takeAccessToken(){
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if(!str) return null;
    const authObj = JSON.parse(str)
    if(authObj.expire <= new Date()){
        deleteAccessToken()
        ElMessage.warning('登陆状态已过期，请重新登录')
        return null
    }
    return authObj.token
}

function storeAccessToken(token, remember, expire){
    const authObj = {token:token, expire:expire}
    const str = JSON.stringify(authObj)
    if(remember)
        localStorage.setItem(authItemName, str)
    
    else
        sessionStorage.setItem(authItemName, str)
}

function deleteAccessToken(){
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function internalPost(url, data, header, success, failure, error = defaultError){
    axios.post(url, data, {headers: header}).then(({data}) => {
        if(data.code == 200)
            success(data.data)
        else
            failure(data.message, data.code, url)
    }).catch(err => error(err))
}

function internalGet(url, header, success, failure, error = defaultError){
    axios.get(url, {headers: header}).then(({data}) => {
        if(data.code === 200)
            success(data.data)
        else
            failure(data.message, data.code, url)
    }).catch(err => error(err))
}

function get(url, success, failure = defaultFailure){
    internalGet(url, accessHeader(), success, failure)
}

function post(url, data, success, failure = defaultFailure){
    internalPost(url, data, accessHeader(), success, failure)
}

function login(username, password, remember, success, failure = defaultFailure){
    internalPost('/api/auth/login', {
        username: username,
        password: password
    },{
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data) => {
        storeAccessToken(data.token, remember, data.expire)
        ElMessage.success(`登录成功，欢迎 ${data.username}`)
        success(data)
    }, failure)
}

function logout(success, failure = defaultFailure){
    get('/api/auth/logout', () => {
        deleteAccessToken()
        ElMessage.success('退出登录成功，欢迎再次使用')
        success()
    }, failure)
}

function unauthorized(){
    return !takeAccessToken() //如果访问令牌不存在或已过期，它会返回 true，表示用户未授权
                              //假设 takeAccessToken() 返回有效的令牌对象（即用户已授权）。那么 !takeAccessToken() 会把有效对象转换为 false，因为在 JavaScript 中，任何非空对象都被视为 true，而 ! 操作符将其反转为 false。
}

export {login, logout, get, post, unauthorized}
