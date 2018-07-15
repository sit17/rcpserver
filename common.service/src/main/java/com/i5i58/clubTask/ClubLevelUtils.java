package com.i5i58.clubTask;

public class ClubLevelUtils {
	public static final long MaxUserScore = 210L;
	public enum ClubScore{
		LEVELSCORE1(1, 9600L, "初出茅庐"),
		LEVELSCORE2(2, 23700L, "新秀崛起"),
		LEVELSCORE3(3, 51000L, "新秀崛起"),
		LEVELSCORE4(4, 96000L, "明日之星"),
		LEVELSCORE5(5, 215000L, "大牌网红"),
		LEVELSCORE6(6, 4716000L, "超级巨星"),
		LEVELSCORE_INVALID(0, 0, "");
		ClubScore(int level, long maxScore, String title){
			this.level = level;
			this.maxScore = maxScore;
			this.title = title;
		}
		private int level;
		private long maxScore;
		private String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public long getMaxScore() {
			return maxScore;
		}
		public void setMaxScore(long maxScore) {
			this.maxScore = maxScore;
		}
		
		public static ClubScore getClubScoreByLevel(int level){
			switch (level) {
			case 1:
				return LEVELSCORE1;
			case 2:
				return LEVELSCORE2;
			case 3:
				return LEVELSCORE3;
			case 4:
				return LEVELSCORE4;
			case 5:
				return LEVELSCORE5;
			case 6:
				return LEVELSCORE6;
			default:
				return LEVELSCORE_INVALID;
			}
		}
		public static ClubScore getClubScoreByMaxScore(long score){
			if (score <= 9600){
				return LEVELSCORE1;
			}else if (score <= 23700){
				return LEVELSCORE2;
			}else if (score <= 51000){
				return LEVELSCORE3;
			}else if (score <= 96000){
				return LEVELSCORE4;
			}else if (score <= 215000){
				return LEVELSCORE5;
			}else if (score <= 471600){
				return LEVELSCORE6;
			}else {
				return LEVELSCORE_INVALID;
			}
		}
	}

	public static int getClubLevel(long score){
		ClubScore clubScore = ClubScore.getClubScoreByMaxScore(score);
		return clubScore.getLevel();
	}

	public static String getClubTitleByLevel(int level) {
		ClubScore clubScore = ClubScore.getClubScoreByLevel(level);
		return clubScore.getTitle();
	}
	public static String getClubTitleByScore(long score){
		ClubScore clubScore = ClubScore.getClubScoreByMaxScore(score);
		return clubScore.getTitle();
	}
	
	
}
