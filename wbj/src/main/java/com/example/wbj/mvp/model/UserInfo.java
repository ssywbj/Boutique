package com.example.wbj.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wbj on 2017/12/13.
 */
public class UserInfo {
    /**
     * recycleNum : 0
     * serverFailNum : 6
     * rtn : 0
     * completeNum : 1
     * sync : 0
     * tasks : [{"failCode":0,"name":"[电影天堂www.dygod.com]越狱第一季第09集.rmvb","url":"","speed":0,"downTime":458,
     * "subList":[],"createTime":1508495577,"dcdnChannel":{"available":0,"state":0,"failCode":0,"speed":0,"dlBytes":406658172},
     * "state":11,"exist":0,"remainTime":0,"progress":10000,"path":"/app/system/miner.plugin-etm.ipk/tmp/C:/onecloud/tddownload/",
     * "type":1,"id":"150849557738826495","completeTime":1508496480,"size":406150268}]
     * dlNum : 0
     */

    private int recycleNum;
    private int serverFailNum;
    private int rtn;
    private int completeNum;
    private int sync;
    private int dlNum;
    @SerializedName("tasks")
    private List<TaskInfo> taskList;

    public int getRecycleNum() {
        return recycleNum;
    }

    public void setRecycleNum(int recycleNum) {
        this.recycleNum = recycleNum;
    }

    public int getServerFailNum() {
        return serverFailNum;
    }

    public void setServerFailNum(int serverFailNum) {
        this.serverFailNum = serverFailNum;
    }

    public int getRtn() {
        return rtn;
    }

    public void setRtn(int rtn) {
        this.rtn = rtn;
    }

    public int getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(int completeNum) {
        this.completeNum = completeNum;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public int getDlNum() {
        return dlNum;
    }

    public void setDlNum(int dlNum) {
        this.dlNum = dlNum;
    }

    public List<TaskInfo> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskInfo> taskList) {
        this.taskList = taskList;
    }

    public static class TaskInfo {
        /**
         * failCode : 0
         * name : [电影天堂www.dygod.com]越狱第一季第09集.rmvb
         * url :
         * speed : 0
         * downTime : 458
         * subList : []
         * createTime : 1508495577
         * dcdnChannel : {"available":0,"state":0,"failCode":0,"speed":0,"dlBytes":406658172}
         * state : 11
         * exist : 0
         * remainTime : 0
         * progress : 10000
         * path : /app/system/miner.plugin-etm.ipk/tmp/C:/onecloud/tddownload/
         * type : 1
         * id : 150849557738826495
         * completeTime : 1508496480
         * size : 406150268
         */

        private int failCode;
        private String name;
        private String url;
        private int speed;
        private int downTime;
        private int createTime;
        private DcdnChannel dcdnChannel;
        private int state;
        private int exist;
        private int remainTime;
        private int progress;
        private String path;
        private int type;
        private String id;
        private int completeTime;
        private int size;
        private List<?> subList;

        public int getFailCode() {
            return failCode;
        }

        public void setFailCode(int failCode) {
            this.failCode = failCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public int getDownTime() {
            return downTime;
        }

        public void setDownTime(int downTime) {
            this.downTime = downTime;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public DcdnChannel getDcdnChannel() {
            return dcdnChannel;
        }

        public void setDcdnChannel(DcdnChannel dcdnChannel) {
            this.dcdnChannel = dcdnChannel;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getExist() {
            return exist;
        }

        public void setExist(int exist) {
            this.exist = exist;
        }

        public int getRemainTime() {
            return remainTime;
        }

        public void setRemainTime(int remainTime) {
            this.remainTime = remainTime;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(int completeTime) {
            this.completeTime = completeTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<?> getSubList() {
            return subList;
        }

        public void setSubList(List<?> subList) {
            this.subList = subList;
        }

        public static class DcdnChannel {
            /**
             * available : 0
             * state : 0
             * failCode : 0
             * speed : 0
             * dlBytes : 406658172
             */

            private int available;
            private int state;
            private int failCode;
            private int speed;
            private int dlBytes;

            public int getAvailable() {
                return available;
            }

            public void setAvailable(int available) {
                this.available = available;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getFailCode() {
                return failCode;
            }

            public void setFailCode(int failCode) {
                this.failCode = failCode;
            }

            public int getSpeed() {
                return speed;
            }

            public void setSpeed(int speed) {
                this.speed = speed;
            }

            public int getDlBytes() {
                return dlBytes;
            }

            public void setDlBytes(int dlBytes) {
                this.dlBytes = dlBytes;
            }
        }
    }
}
