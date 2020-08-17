package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name="follow")

@NamedQueries({
    @NamedQuery(//ログイン社員が１社員をフォローしているかをカウントで確認
            name = "followedCheck",
            query = "select count(f) from Follow AS f WHERE f.follower_id = :logid AND f.followed_id = :employee "),
    @NamedQuery(//ログイン社員が１社員をフォローしている時フォロー情報を取得
            name = "getFollowData",
            query = "select f from Follow AS f WHERE f.follower_id = :logid AND f.followed_id = :employee "),
    @NamedQuery(//1社員に対するフォロワー数を取得
            name = "getFollowerCount",
            query = "select count(f) from Follow AS f WHERE f.followed_id = :employee "),
})

@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private Employee followed_id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Employee follower_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollowed_id() {
        return followed_id;
    }

    public void setFollowed_id(Employee followd_id) {
        this.followed_id = followd_id;
    }

    public Employee getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(Employee Follower_id) {
        this.follower_id = Follower_id;
    }

}
