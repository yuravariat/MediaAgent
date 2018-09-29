package haifa.university.youtube_provider;

/**
 * Created by yura on 17/03/2016.
 */
public class Converter {
    public static PlaylistItem ConvertFromYouTubeObj(com.google.api.services.youtube.model.PlaylistItem playlistItem){

        if(playlistItem==null){
            return null;
        }

        PlaylistItem item = new PlaylistItem();
        item.etag = playlistItem.getEtag();
        item.id = playlistItem.getId();
        item.kind = playlistItem.getKind();

        if(playlistItem.getContentDetails()!=null) {
            item.contentDetails = new PlaylistItemContentDetails();
            item.contentDetails.endAt = playlistItem.getContentDetails().getEndAt();
            item.contentDetails.note = playlistItem.getContentDetails().getNote();
            item.contentDetails.startAt = playlistItem.getContentDetails().getStartAt();
            item.contentDetails.videoId = playlistItem.getContentDetails().getVideoId();
        }

        if(playlistItem.getSnippet()!=null) {
            item.snippet = new PlaylistItemSnippet();
            item.snippet.channelId = playlistItem.getSnippet().getChannelId();
            item.snippet.channelTitle = playlistItem.getSnippet().getChannelTitle();
            item.snippet.description = playlistItem.getSnippet().getDescription();
            item.snippet.playlistId = playlistItem.getSnippet().getPlaylistId();
            item.snippet.position = playlistItem.getSnippet().getPosition();
            item.snippet.publishedAt = playlistItem.getSnippet().getPublishedAt();
            item.snippet.title = playlistItem.getSnippet().getTitle();

            if(playlistItem.getSnippet().getResourceId()!=null){
                item.snippet.resourceId = new ResourceId();
                item.snippet.resourceId.channelId = playlistItem.getSnippet().getResourceId().getChannelId();
                item.snippet.resourceId.kind = playlistItem.getSnippet().getResourceId().getKind();
                item.snippet.resourceId.playlistId = playlistItem.getSnippet().getResourceId().getPlaylistId();
                item.snippet.resourceId.videoId = playlistItem.getSnippet().getResourceId().getVideoId();
            }

            com.google.api.services.youtube.model.ThumbnailDetails thumbnailsDetails = playlistItem.getSnippet().getThumbnails();
            if(thumbnailsDetails!=null){
                item.snippet.thumbnails = new ThumbnailDetails();
                com.google.api.services.youtube.model.Thumbnail default__ = thumbnailsDetails.getDefault();
                com.google.api.services.youtube.model.Thumbnail high = thumbnailsDetails.getHigh();
                com.google.api.services.youtube.model.Thumbnail maxres = thumbnailsDetails.getMaxres();
                com.google.api.services.youtube.model.Thumbnail medium = thumbnailsDetails.getMedium();
                com.google.api.services.youtube.model.Thumbnail standard = thumbnailsDetails.getStandard();

                if(default__!=null) {
                    item.snippet.thumbnails.default__ =
                            new Thumbnail(default__.getUrl(),default__.getHeight(),default__.getWidth());
                }
                if(high!=null) {
                    item.snippet.thumbnails.high =
                            new Thumbnail(high.getUrl(),high.getHeight(),high.getWidth());
                }
                if(maxres!=null) {
                    item.snippet.thumbnails.default__ =
                            new Thumbnail(maxres.getUrl(),maxres.getHeight(),maxres.getWidth());
                }
                if(medium!=null) {
                    item.snippet.thumbnails.default__ =
                            new Thumbnail(medium.getUrl(),medium.getHeight(),medium.getWidth());
                }
                if(standard!=null) {
                    item.snippet.thumbnails.default__ =
                            new Thumbnail(standard.getUrl(),standard.getHeight(),standard.getWidth());
                }
            }
        }

        if(playlistItem.getStatus()!=null) {
            item.status = new PlaylistItemStatus();
            item.status.privacyStatus = playlistItem.getStatus().getPrivacyStatus();
        }

        return item;
    }
}
