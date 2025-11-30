CREATE TABLE "User" (
  "id" varchar PRIMARY KEY NOT NULL,
  "email" varchar UNIQUE,
  "password" varchar,
  "nickname" varchar UNIQUE,
  "profile_image_url" varchar,
  "createdAt" timestamp NOT NULL
);

CREATE TABLE "Account" (
  "id" varchar PRIMARY KEY NOT NULL,
  "userId" varchar NOT NULL,
  "provider" varchar NOT NULL,
  "providerAccountId" varchar NOT NULL,
  "access_token" varchar,
  "refresh_token" varchar,
  "expires_at" integer,
  "user" "User"
);

CREATE TABLE "Session" (
  "id" varchar PRIMARY KEY NOT NULL,
  "sessionToken" varchar UNIQUE NOT NULL,
  "userId" varchar NOT NULL,
  "expires" timestamp NOT NULL,
  "user" "User"
);

CREATE TABLE "Post" (
  "id" varchar PRIMARY KEY NOT NULL,
  "userId" varchar NOT NULL,
  "url" varchar NOT NULL,
  "title" varchar,
  "description" text,
  "ogImage" varchar,
  "aiSummary" text,
  "content" text,
  "viewCount" integer DEFAULT 0,
  "createdAt" timestamp NOT NULL,
  "user" "User"
);

CREATE TABLE "Comment" (
  "id" varchar PRIMARY KEY NOT NULL,
  "userId" varchar NOT NULL,
  "postId" varchar NOT NULL,
  "content" text NOT NULL,
  "createdAt" timestamp NOT NULL,
  "updatedAt" timestamp NOT NULL,
  "user" "User",
  "post" "Post"
);

CREATE TABLE "Bookmark" (
  "userId" varchar NOT NULL,
  "postId" varchar NOT NULL,
  "createdAt" timestamp NOT NULL,
  "user" "User",
  "post" "Post",
  PRIMARY KEY ("postId", "userId")
);

CREATE TABLE "Tag" (
  "id" varchar PRIMARY KEY NOT NULL,
  "name" varchar UNIQUE NOT NULL
);

CREATE TABLE "PostTag" (
  "postId" varchar NOT NULL,
  "tagId" varchar NOT NULL,
  "post" "Post",
  "tag" "Tag",
  PRIMARY KEY ("postId", "tagId")
);

CREATE TABLE "PostLike" (
  "userId" varchar NOT NULL,
  "postId" varchar NOT NULL,
  "createdAt" timestamp NOT NULL,
  "user" "User",
  "post" "Post",
  PRIMARY KEY ("postId", "userId")
);

CREATE UNIQUE INDEX ON "Account" ("provider", "providerAccountId");

ALTER TABLE "Account" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "Session" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "Post" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "Comment" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "Comment" ADD FOREIGN KEY ("postId") REFERENCES "Post" ("id");

ALTER TABLE "Bookmark" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "Bookmark" ADD FOREIGN KEY ("postId") REFERENCES "Post" ("id");

ALTER TABLE "PostLike" ADD FOREIGN KEY ("userId") REFERENCES "User" ("id");

ALTER TABLE "PostLike" ADD FOREIGN KEY ("postId") REFERENCES "Post" ("id");

ALTER TABLE "PostTag" ADD FOREIGN KEY ("postId") REFERENCES "Post" ("id");

ALTER TABLE "PostTag" ADD FOREIGN KEY ("tagId") REFERENCES "Tag" ("id");
