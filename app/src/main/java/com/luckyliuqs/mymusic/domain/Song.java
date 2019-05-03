package com.luckyliuqs.mymusic.domain;

/**
 * 歌曲实体类
 */
public class Song{
    public static final String[] SORTKEYS = new String[]{"id", "title", "album_title"};

    /**
     * 在线音乐
     */
    public static final int SOURCE_ONLINE = 0;

    /**
     * 本地音乐
     */
    public static final int SOURCE_LOCAL = 1;

    /**
     * 下载的音乐
     */
    public static final int SOURCE_DOWNLOAD = 2;

    /**
     * 主键
     */
    private String id;

    /**
     * 歌曲名称
     */
    private String title;

    /**
     * 歌曲封面
     */
    private String banner;

    /**
     * 歌曲被点击数
     */
    private long clicks_count;

    /**
     * 歌曲被评论数
     */
    private long comments_count;

    /**
     * 歌手
     */
    private User artist;

    /**
     * 歌手名称
     */
    private String artist_name;

    /**
     * 歌手头像
     */
    private String artist_avatar;

    /**
     * 歌手ID，只有在线音乐才有
     */
    private String artist_id;

    /**
     * 音乐网络路径
     */
    private String uri;

    /**
     * 歌词
     */
    private Lyric lyric;

    /**
     * 歌词ID
     */
    private String lyric_id;

    /**
     * 歌词类型
     */
    private int lyric_type;

    /**
     * 歌词内容
     */
    private String lyric_content;

    /**
     * 专辑
     */
    private Album album;

    /**
     * 专辑ID
     */
    private String album_id;

    /**
     * 专辑名称
     */
    private String album_title;

    /**
     * 专辑歌手ID
     */
    private String album_artist_id;

    /**
     * 专辑歌手名称
     */
    private String album_artist_name;

    /**
     * 专辑封面
     */
    private String album_banner;

    /**
     * 专辑发行日期
     */
    private String album_released_at;

    /**
     * 歌曲时长：在线音乐播放后，才可以取值
     */
    private long duration;

    /**
     * 用户ID，保存到数据库，实现多用户
     */
    private String userId;

    /**
     * 歌曲的来源：0-在线音乐，1-本地音乐，2-下载音乐
     */
    private int source;

    /**
     * 歌曲是否在播放列表
     */
    private boolean playList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(long clicks_count) {
        this.clicks_count = clicks_count;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public User getArtist() {
        return artist;
    }

    public void setArtist(User artist) {
        this.artist = artist;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getArtist_avatar() {
        return artist_avatar;
    }

    public void setArtist_avatar(String artist_avatar) {
        this.artist_avatar = artist_avatar;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Lyric getLyric() {
        return lyric;
    }

    public void setLyric(Lyric lyric) {
        this.lyric = lyric;

        //歌词
        this.lyric_id = lyric.getId();
        this.lyric_type = lyric.getStyle();
        this.lyric_content = lyric.getContent();
    }

    public String getLyric_id() {
        return lyric_id;
    }

    public void setLyric_id(String lyric_id) {
        this.lyric_id = lyric_id;
    }

    public int getLyric_type() {
        return lyric_type;
    }

    public void setLyric_type(int lyric_type) {
        this.lyric_type = lyric_type;
    }

    public String getLyric_content() {
        return lyric_content;
    }

    public void setLyric_content(String lyric_content) {
        this.lyric_content = lyric_content;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getAlbum_artist_id() {
        return album_artist_id;
    }

    public void setAlbum_artist_id(String album_artist_id) {
        this.album_artist_id = album_artist_id;
    }

    public String getAlbum_artist_name() {
        return album_artist_name;
    }

    public void setAlbum_artist_name(String album_artist_name) {
        this.album_artist_name = album_artist_name;
    }

    public String getAlbum_banner() {
        return album_banner;
    }

    public void setAlbum_banner(String album_banner) {
        this.album_banner = album_banner;
    }

    public String getAlbum_released_at() {
        return album_released_at;
    }

    public void setAlbum_released_at(String album_released_at) {
        this.album_released_at = album_released_at;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public boolean isPlayList() {
        return playList;
    }

    //设置歌曲是否在播放列表里面
    public void setPlayList(boolean playList) {
        this.playList = playList;
    }

    /**
     * 判断传入的歌曲是否是当前的歌曲
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }else if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        Song song = (Song) obj;
        //如果ID相等，则代表是当前歌曲，返回true
        return id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * 填充专辑和歌曲信息
     */
    public void fill(){
        //专辑
        this.album_id = album.getId();
        this.album_title = album.getTitle();
        this.album_banner = album.getBanner();
        //this.album_artist_id=album.getArtist().getId();
        //this.album_artist_name=album.getArtist().getNickname();
        //this.album_released_at=album.getReleased_at();

        //歌手
        this.artist_id=artist.getId();
        this.artist_name=artist.getNickname();
        this.artist_avatar=artist.getAvatar();
    }

    /**
     * 判断当前歌曲是否为本地歌曲
     * @return
     */
    public boolean isLocal(){
        return source == SOURCE_LOCAL;
    }

}
