package com.fierydragon.components.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.fierydragon.components.TurnController;
import com.fierydragon.components.enums.CardType;
import com.fierydragon.components.interfaces.ICardDelegate;
import com.fierydragon.components.level.object.CardController;
import com.fierydragon.components.level.object.CardFactory;
import com.fierydragon.components.level.object.PlayerController;
import com.fierydragon.components.level.object.VolcanoCard;
import com.fierydragon.core.framework.GameModeBase;
import com.fierydragon.core.framework.ResourceLoader;
import com.fierydragon.core.level.GameLevel;

import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * The GameBoard class represents the main game board and manages the game state, player movements, and card actions.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public class GameBoard extends GameLevel implements ICardDelegate {
    // Changed variables in init() (used to store coordinates of graphics)
    private ArrayList<Point2D> caveCoordinates;
    private ArrayList<Point2D> tileCoordinates;

    // Unchanged variables in init()
    private GameModeBase gameMode;
    private TurnController turnController;
    private ArrayList<Pair<CardType, Integer>> dragonCardValues;
    private ArrayList<CardType> tiles;
    private ArrayList<CardType> caves;
    private ArrayList<VolcanoCard> volcanoCards;
    private ArrayList<CardController> dragonCards;
    private ArrayList<PlayerController> players;
    private ArrayList<Boolean> tileOccupation;

    // Used for calculation of movement path (Will change for each init() pass)
    private double boardPathRadius;
    private int caveSpacing;

    // Used for keeping track of turn number
    private Text turnIndicator;

    // Used for board background
    private ImageView boardBackgroundView;

    /**
     * Constructs a GameBoard object.
     *
     * @param gameMode the game mode of the game
     * @param turnController the turn controller managing player turns
     * @param caves the list of cave card types
     * @param tileTypes the list of tile card types
     * @param tileNum the total number of tiles
     * @param dragonCardValues the list of dragon card values
     */
    public GameBoard(GameModeBase gameMode, TurnController turnController, ArrayList<CardType> caves, ArrayList<CardType> tileTypes, int volcanoCardNum, ArrayList<Pair<CardType, Integer>> dragonCardValues) {
        this.sceneName = "GameBoard";

        this.gameMode = gameMode;
        this.turnController = turnController;
        this.caves = caves;
        this.dragonCardValues = dragonCardValues;
        this.volcanoCards = new ArrayList<VolcanoCard>();

        // ===============
        // Adding null as first segment (movement starts from cave thus index 0)
        // segments start from index 1
        int tileNum = volcanoCardNum * 3;

        this.tiles = new ArrayList<CardType>();
        this.tiles.add(null);
        ArrayList<CardType> tileDupList = new ArrayList<CardType>();
        for(int i = 0; i < tileNum; i++) {
            tileDupList.add(tileTypes.get(i % tileTypes.size()));
        }
        Collections.shuffle(tileDupList);
        this.tiles.addAll(tileDupList);

        for(int i = 0; i < tileDupList.size(); i = i + 3) {
            ArrayList<CardType> tileSet = new ArrayList<CardType>();
            tileSet.add(tileDupList.get(i));
            tileSet.add(tileDupList.get(i + 1));
            tileSet.add(tileDupList.get(i + 2));
            this.volcanoCards.add(new VolcanoCard(tileSet));
        }
        // ===============
        
        // ===============
        // Used for keeping track of segment occupied by players
        this.tileOccupation = new ArrayList<Boolean>(Collections.nCopies(this.tiles.size(), false));
        // ===============

        // ===============
        // Setting up class scope variables
        // Used for calculation of movement path and caveSpacing overall
        this.caveSpacing = (int) ((tileNum) / this.caves.size());
        this.boardPathRadius = 0.0;
        // ===============

        // ===============
        // Setting up playerControllers and cardControllers
        // Setup PlayerControllers first as CardControllers need them as delegates
        this.players = new ArrayList<PlayerController>();
        for(int i = 0; i < this.caves.size(); i++) {
            PlayerController player = new PlayerController(this, this.caves.get(i), (i * caveSpacing) + 1, this.tiles.size() - 1, i);
            this.players.add(player);
        }
        // set player n in position to win with only 2 moves(Testing purposes)
        // TODO: REMOVE/COMMENT BEFORE RELEASE
        // this.players.get(0).setCurrentTileID(24);
        // this.players.get(0).setCurrentTileType(this.tiles.get(24));
        // this.players.get(0).setTilesLeft(2);
        // this.tileOccupation.set(24, true);
        // this.players.get(1).setCurrentTileID(1);
        // this.players.get(1).setCurrentTileType(this.tiles.get(1));
        // this.players.get(1).setTilesLeft(13);
        // this.tileOccupation.set(1, true);
        // end of test line





        this.dragonCards = new ArrayList<CardController>();
        Collections.shuffle(this.dragonCardValues);
        for(int i = 0; i < this.dragonCardValues.size(); i++) {
            CardController dragonCard = CardFactory.createCardController(this,this.dragonCardValues.get(i).getKey(), this.dragonCardValues.get(i).getValue());
            dragonCard.setDelegate(this);
            this.dragonCards.add(dragonCard);
        }
        // ===============

        // ===============
        // Setting up board background
        String backgroundPath = ResourceLoader.ASSETS.getPath() + "background.png";
        this.boardBackgroundView = new ImageView(new Image(backgroundPath));
        // ===============
    }

    /**
     * Constructs a GameBoard object from a map of properties. (Loaded from save file)
     *
     * @param gameMode the game mode of the game
     * @param turnController the turn controller managing player turns
     * @param properties the map of properties
     */
    public GameBoard(GameModeBase gameMode, TurnController turnController, Map<String, String> properties) {
        this.sceneName = "GameBoard";

        this.gameMode = gameMode;
        this.turnController = turnController;

        int tileNum = Integer.parseInt(properties.get("volcanoCardCount")) * 3;
        int caveNum = Integer.parseInt(properties.get("playerCount"));
        int volcanoCardNum = tileNum / 3;

        // ===============
        // Setup of the game board caves and tiles based on properties
        this.caves = new ArrayList<CardType>();
        for(int i = 0; i < caveNum; i++) {
            this.caves.add(CardType.valueOf(properties.get("cave" + (i + 1))));
        }

        this.volcanoCards = new ArrayList<VolcanoCard>();
        for(int i = 0; i < volcanoCardNum; i++) {
            ArrayList<CardType> tileSet = new ArrayList<CardType>();
            tileSet.add(CardType.valueOf(properties.get("volcanoCard" + (i + 1) + ".tile1")));
            tileSet.add(CardType.valueOf(properties.get("volcanoCard" + (i + 1) + ".tile2")));
            tileSet.add(CardType.valueOf(properties.get("volcanoCard" + (i + 1) + ".tile3")));
            this.volcanoCards.add(new VolcanoCard(tileSet));
        }

        this.tiles = new ArrayList<CardType>();
        this.tiles.add(null);
        for(VolcanoCard volcanoCard : this.volcanoCards) {
            for(CardType tileType : volcanoCard.getTileTypes()) {
                this.tiles.add(tileType);
            }
        }
        // ===============

        // ===============
        // Setting up class scope variables
        // Used for calculation of movement path and caveSpacing overall
        this.caveSpacing = (int) Math.floor((tileNum) / this.caves.size());
        this.boardPathRadius = 0.0;
        // ===============

        // ===============
        // Setting up playerControllers and cardControllers
        // Setup PlayerControllers first as CardControllers need them as delegates
        this.tileOccupation = new ArrayList<Boolean>(Collections.nCopies(this.tiles.size(), false));

        this.players = new ArrayList<PlayerController>();
        for(int i = 0; i < this.caves.size(); i++) {
            int playerCaveTileID = Integer.parseInt(properties.get("player" + (i + 1) + ".caveTileID"));
            int playerCurrentTileID = Integer.parseInt(properties.get("player" + (i + 1) + ".currentTileID"));
            CardType playerCurrentTileType = CardType.valueOf(properties.get("player" + (i + 1) + ".currentTileType"));
            int playerTilesLeft = Integer.parseInt(properties.get("player" + (i + 1) + ".tilesLeft"));

            PlayerController player = new PlayerController(this, this.caves.get(i), playerCaveTileID, this.tiles.size() - 1, i);
            player.setCurrentTileID(playerCurrentTileID);
            player.setCurrentTileType(playerCurrentTileType);
            player.setTilesLeft(playerTilesLeft);
            this.players.add(player);

            if(playerCurrentTileID != 0) {
                this.tileOccupation.set(playerCurrentTileID, true);
            }
        }

        this.dragonCards = new ArrayList<CardController>();
        for(int i = 0; i < Integer.parseInt(properties.get("cardCount")); i++) {
            CardType cardType = CardType.valueOf(properties.get("dragonCard" + (i + 1) + ".cardType"));
            int cardValue = Integer.parseInt(properties.get("dragonCard" + (i + 1) + ".cardValue"));
            boolean cardFlipped = Boolean.parseBoolean(properties.get("dragonCard" + (i + 1) + ".flipped"));

            CardController dragonCard = CardFactory.createCardController(this, cardType, cardValue);
            
            if(cardFlipped) {
                dragonCard.flipOver();
            }

            dragonCard.setDelegate(this);
            this.dragonCards.add(dragonCard);
        }
        // ===============


        // ===============
        // Setting up board background
        String backgroundPath = ResourceLoader.ASSETS.getPath() + "background.png";
        this.boardBackgroundView = new ImageView(new Image(backgroundPath));
        // ===============
    }

    @Override
    public void init() {
        if(this.scene.getWidth() > 0 && this.scene.getHeight() > 0) {
            // Clearing of old coordinates
            this.levelRoot.getChildren().clear();
            this.tileCoordinates = new ArrayList<Point2D>(this.tiles.size());
            this.tileCoordinates.add(null);
            this.caveCoordinates = new ArrayList<Point2D>(this.caves.size());

            // Level center values
            double centerX = this.scene.getWidth() / 2;
            double centerY = this.scene.getHeight() / 2;

            // ===============
            // Board Background (Run this before all other scene Node creation)
            this.boardBackgroundView.setX(0);
            this.boardBackgroundView.setY(0);
            this.boardBackgroundView.setFitWidth(this.scene.getWidth());
            this.boardBackgroundView.setFitHeight(this.scene.getHeight());
            this.boardBackgroundView.setOpacity(0.8);

            this.levelRoot.getChildren().add(this.boardBackgroundView);
            // ===============

            // ===============
            // Turn indicator placement
            double turnIndicatorX = 0.01 * this.scene.getWidth();
            double turnIndicatorY = 0.01 * this.scene.getHeight();
            this.turnIndicator = new Text(this.getTurnPlayerString());
            this.turnIndicator.setFont(new Font(0.06 * this.scene.getHeight()));
            this.turnIndicator.setFill(this.caves.get(this.turnController.getTurnPlayerID()).getColor());
            this.turnIndicator.relocate(turnIndicatorX, turnIndicatorY);

            this.levelRoot.getChildren().add(this.turnIndicator);
            // ==============

            // ===============
            // Board creation
            // Board center values
            double boardCenterX = centerX;
            double boardCenterY = centerY;

            // Board values outside of creation needs
            // Board various radii
            double boardOuterRadius = 0.75 * (this.scene.getHeight() / 2);
            double boardInnerRadius = 0.65 * boardOuterRadius;
            double centerToCaveRadius = 0.0;
            double caveRadius = 0.0;
            double dragonCardRadius = 0.1 * boardOuterRadius;
            ArrayList<Double> dragonCardRingRadii = new ArrayList<Double>(Arrays.asList(
                0.25 * boardOuterRadius,
                0.5 * boardOuterRadius
            ));

            // Used for determining movement path for player characters
            this.boardPathRadius = 0.85 * boardOuterRadius;

            // Segment spacing
            double tileAngle = 360.0 / (this.tiles.size() - 1);
            // double tileAngleOffset = (tileAngle / 2.0) + tileAngle;
            double tileAngleOffset = (tileAngle / 2.0);

            // Minimum chord length
            double minChordLength = 0.0;

            // ===============
            // Segment & Cave creation
            for (int i = 1; i < this.tiles.size(); i++) {
                double angle1 = (i * tileAngle) + tileAngleOffset;
                double angle2 = ((i + 1) * tileAngle) + tileAngleOffset;
                double tileMidAngle = angle1 + (tileAngle / 2.0);
                
                double x1 = boardCenterX + (boardInnerRadius * Math.cos(Math.toRadians(angle1)));
                double y1 = boardCenterY + (boardInnerRadius * Math.sin(Math.toRadians(angle1)));
                
                double x2 = boardCenterX + (boardOuterRadius * Math.cos(Math.toRadians(angle1)));
                double y2 = boardCenterY + (boardOuterRadius * Math.sin(Math.toRadians(angle1)));

                double x3 = boardCenterX + (boardInnerRadius * Math.cos(Math.toRadians(angle2)));
                double y3 = boardCenterY + (boardInnerRadius * Math.sin(Math.toRadians(angle2)));

                double x4 = boardCenterX + (boardOuterRadius * Math.cos(Math.toRadians(angle2)));
                double y4 = boardCenterY + (boardOuterRadius * Math.sin(Math.toRadians(angle2)));

                double tileMidX = boardCenterX + (boardPathRadius * Math.cos(Math.toRadians(tileMidAngle)));
                double tileMidY = boardCenterY + (boardPathRadius * Math.sin(Math.toRadians(tileMidAngle)));

                minChordLength = Math.sqrt(Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));

                Path tile = new Path();
                tile.getElements().addAll(
                    new MoveTo(x1, y1),
                    new LineTo(x2, y2),
                    new ArcTo(boardOuterRadius, boardOuterRadius, 0, x4, y4, false, true),
                    new LineTo(x3, y3),
                    new ArcTo(boardOuterRadius, boardOuterRadius, 0, x1, y1, false, false)
                );
                tile.setFill(Color.WHITESMOKE);
                
                this.tileCoordinates.add(new Point2D(tileMidX, tileMidY));
                this.levelRoot.getChildren().add(tile);

                double tileImageSize = 1 * minChordLength;
                double tileImageAngle = angle1 + (tileAngle / 2.0);
                double tileImageX = boardCenterX + ((boardInnerRadius + (tileImageSize / 2.0)) * Math.cos(Math.toRadians(tileImageAngle)));
                double tileImageY = boardCenterY + ((boardInnerRadius + (tileImageSize / 2.0)) * Math.sin(Math.toRadians(tileImageAngle)));

                Image tileImage = new Image(ResourceLoader.ASSETS.getPath() + this.tiles.get(i).getResourcePath() + "chit_1.png");
                ImageView tileImageView = new ImageView(tileImage);
                tileImageView.setOpacity(0.8);
                tileImageView.setFitWidth(tileImageSize);
                tileImageView.setFitHeight(tileImageSize);
                tileImageView.setX(tileImageX - (tileImageSize / 2.0));
                tileImageView.setY(tileImageY - (tileImageSize / 2.0));

                this.levelRoot.getChildren().add(tileImageView);

                if((i - 1) % this.caveSpacing == 0 && (i - 1) / this.caveSpacing < this.caves.size()) {
                    double tileLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                    caveRadius = tileLength / 2;

                    double chordMidX = (x2 + x4) / 2;
                    double chordMidY = (y2 + y4) / 2;
                    double chordLength = Math.sqrt(Math.pow(x2 - x4, 2) + Math.pow(y2 - y4, 2));
                    centerToCaveRadius = Math.sqrt(Math.pow(boardCenterX - chordMidX, 2) + Math.pow(boardCenterY - chordMidY, 2)) + Math.sqrt((Math.pow(caveRadius, 2) - Math.pow(chordLength / 2, 2)));

                    double caveMidX = boardCenterX + (centerToCaveRadius * Math.cos(Math.toRadians(tileMidAngle)));
                    double caveMidY = boardCenterY + (centerToCaveRadius * Math.sin(Math.toRadians(tileMidAngle)));

                    CardType caveType = this.caves.get((int) ((i - 1) / this.caveSpacing));

                    Image caveImage = new Image(ResourceLoader.ASSETS.getPath() + caveType.getResourcePath() + "cave.png");
                    ImageView caveImageView = new ImageView(caveImage);
                    caveImageView.setOpacity(0.8);
                    caveImageView.setFitWidth(caveRadius * 2);
                    caveImageView.setFitHeight(caveRadius * 2);
                    caveImageView.setX(caveMidX - (caveRadius));
                    caveImageView.setY(caveMidY - (caveRadius));

                    // Add cave
                    Path cave = new Path();
                    cave.getElements().addAll(
                        new MoveTo(x2, y2),
                        new ArcTo(caveRadius, caveRadius, 0, x4, y4, true, true),
                        new ArcTo(caveRadius, caveRadius, 0, x2, y2, false, true)
                    );
                    cave.setFill(Color.WHITE);
                    cave.setStroke(caveType.getColor());
                    cave.setStrokeWidth(3.0);

                    this.caveCoordinates.add(new Point2D(
                        caveMidX,
                        caveMidY
                    ));
                    this.levelRoot.getChildren().add(cave);
                    this.levelRoot.getChildren().add(caveImageView);
                }
            }
            // ===============

            // ===============
            // Add Dragon Cards (first card always in middle)
            Circle dragonCardShape;
            CardController dragonCard;

            dragonCardShape = new Circle(boardCenterX, boardCenterY, dragonCardRadius, Color.WHITE);
            dragonCard = this.dragonCards.get(0);
            dragonCard.setBounds(dragonCardShape);

            if(this.dragonCards.size() > 1) {
                int cardsLeft = this.dragonCards.size() - 1;
                for(Double radius: dragonCardRingRadii) {
                    int cardsPerRing = Math.min((int) Math.floor((2 * Math.PI * radius) / (dragonCardRadius * 2.2)), cardsLeft);

                    double cardAngle = 360.0 / cardsPerRing;
                    
                    for(int i = 0; i < cardsPerRing; i++) {
                        double angle = cardAngle * i;
                        double cardX = boardCenterX + (radius * Math.cos(Math.toRadians(angle)));
                        double cardY = boardCenterY + (radius * Math.sin(Math.toRadians(angle)));

                        dragonCardShape = new Circle(cardX, cardY, dragonCardRadius, Color.WHITE);
                        dragonCard = this.dragonCards.get(this.dragonCards.size() - cardsLeft);
                        dragonCard.setBounds(dragonCardShape);

                        cardsLeft--;
                    }
                }

                for(CardController card: this.dragonCards) {
                    this.levelRoot.getChildren().add(card.getRoot());
                }
            }
            // ===============

            // ===============
            // Add player
            double playerCharacterRadius = 0.25 * minChordLength;
    
            Circle playerCharacterShape;
            PlayerController player;

            for(int i = 0; i < this.players.size(); i++) {
                player = this.players.get(i);

                if(this.players.get(i).getCurrentTileID() == 0) {
                    playerCharacterShape = new Circle(this.caveCoordinates.get(i).getX(), this.caveCoordinates.get(i).getY(), playerCharacterRadius, this.caves.get(i).getColor());
                } else {
                    int currentTileID = this.players.get(i).getCurrentTileID();
                    playerCharacterShape = new Circle(this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY(), playerCharacterRadius, this.caves.get(i).getColor());
                }

                playerCharacterShape.setStroke(this.caves.get(i).getColor().darker());
                playerCharacterShape.setStrokeWidth(0.2 * playerCharacterRadius);
                player.setBounds(playerCharacterShape);
            }

            for(PlayerController playerController: this.players) {
                this.levelRoot.getChildren().add(playerController.getRoot());
            }
            // ===============

            // ===============
            // Hamburger pause menu button
            double hamburgerButtonLength = 0.05 * this.scene.getWidth();
            double hamburgerButtonHeight = hamburgerButtonLength;
            double hamburgerButtonX = (0.99 * this.scene.getWidth()) - hamburgerButtonLength;
            double hamburgerBUttonY = (0.01 * this.scene.getHeight());

            Rectangle hamburgerButton = new Rectangle(hamburgerButtonX, hamburgerBUttonY, hamburgerButtonLength, hamburgerButtonHeight);
            hamburgerButton.setFill(new Color(0.98, 0.34, 0.29, 0.8));
            hamburgerButton.setArcHeight(hamburgerButtonHeight * 0.5);
            hamburgerButton.setArcWidth(hamburgerButtonHeight * 0.5);

            hamburgerButton.setOnMouseClicked((e) -> {
                this.gameMode.handlePause();
            });

            Text hamburgerButtonText = new Text("\u2630");
            hamburgerButtonText.setFill(Color.WHITE);
            hamburgerButtonText.setFont(new Font(hamburgerButtonHeight * 0.8));

            double hamburgerButtonTextX = hamburgerButtonX + 0.5 * (hamburgerButtonLength - hamburgerButtonText.getLayoutBounds().getWidth());
            double hamburgerButtonTextY = hamburgerBUttonY + 0.5 * (hamburgerButtonHeight - hamburgerButtonText.getLayoutBounds().getHeight());
            hamburgerButtonText.relocate(hamburgerButtonTextX, hamburgerButtonTextY);

            hamburgerButtonText.setOnMouseClicked((e) -> {
                this.gameMode.handlePause();
            });

            this.levelRoot.getChildren().addAll(hamburgerButton, hamburgerButtonText);
            // ===============




        }
    }

    @Override
    public String getSceneName() {
        return this.sceneName;
    }

    /**
     * process the next player
     */
    @Override
    public void nextTurn() {
        this.turnController.nextTurn();

        this.turnIndicator.setText(this.getTurnPlayerString());
        this.turnIndicator.setFill(this.caves.get(this.turnController.getTurnPlayerID()).getColor());

        for(CardController card: this.dragonCards) {
            card.flipBack();
        }
    }

    /**
     * Checks if a player can move to the specified tile.
     *
     * @param tileID the ID of the tile to check
     * @return true if the player can move to the tile, false otherwise
     */
    public boolean canMove(int tileID) {
        return this.tileOccupation.get(tileID);
    }

    /**
     * execute the logic of handling card when clicked
     * @param cardType type of the card that is being handled
     * @param cardValue integer value of the card being handled
     */
    @Override
    public void handleCardAction(CardType cardType, int cardValue) {
        Path path = new Path();
    
        int playerID = this.turnController.getTurnPlayerID();
        PlayerController player = this.players.get(playerID);
        int previousTileID = player.getCurrentTileID();
        int currentTileID = player.getCurrentTileID();
    
        if (cardType == player.getCurrentTileType() && cardValue <= player.getTilesLeft()) {
            if (currentTileID == 0 && cardValue > 0) {
                currentTileID = player.getCaveTileID();
    
                path.getElements().addAll(
                    new MoveTo(this.caveCoordinates.get(player.getCurrentCaveID()).getX(),this.caveCoordinates.get(player.getCurrentCaveID()).getY()),
                    new LineTo(this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY())
                );
    
                cardValue--;
            }
    
            if (cardValue > 0) {
                path.getElements().add(
                    new MoveTo(this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY())
                );
    
                if (cardValue == player.getTilesLeft()) {
                    cardValue--;
                    currentTileID += cardValue;
    
                    if (currentTileID > (this.tiles.size() - 1)) {
                        currentTileID -= (this.tiles.size() - 1);
                    }
    
                    path.getElements().addAll(
                        new ArcTo(this.boardPathRadius, this.boardPathRadius, 0, this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY(), false, true),
                        new LineTo(this.caveCoordinates.get(playerID).getX(), this.caveCoordinates.get(playerID).getY())
                    );
    
                    cardValue++;
                    currentTileID = 0;
                } else {
                    currentTileID += cardValue;
    
                    if (currentTileID > (this.tiles.size() - 1)) {
                        currentTileID -= (this.tiles.size() - 1);
                    }
    
                    path.getElements().add(
                        new ArcTo(this.boardPathRadius, this.boardPathRadius, 0, this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY(), false, true)
                    );
                }
            }
        }
    
        if (cardValue < 0 && currentTileID > 0) {
            path.getElements().add(
                new MoveTo(this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY())
            );
    
            currentTileID += cardValue;
    
            if (currentTileID <= 0) {
                currentTileID += (this.tiles.size() - 1);
            }
    
            path.getElements().add(
                new ArcTo(this.boardPathRadius, this.boardPathRadius, 0, this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY(), false, false)
            );
        }
    
        if (path.getElements().size() > 0) {
            if (this.tileOccupation.get(currentTileID)) {
                if (previousTileID == 0) {
                    this.nextTurn();
                    return;
                }
    
                PlayerController occupyingPlayer = null;
                for (PlayerController p : this.players) {
                    if (p.getCurrentTileID() == currentTileID) {
                        occupyingPlayer = p;
                        break;
                    }
                }
    
                if (occupyingPlayer != null) {
                    player.setCurrentTileID(currentTileID);
                    player.setCurrentTileType(this.tiles.get(currentTileID));
                    player.setTilesLeft(player.getTilesLeft() - cardValue);
    
                    occupyingPlayer.setCurrentTileID(previousTileID);
                    occupyingPlayer.setCurrentTileType(this.tiles.get(previousTileID));
                    occupyingPlayer.setTilesLeft(occupyingPlayer.getTilesLeft() + cardValue);
    
                    this.tileOccupation.set(previousTileID, true);
                    this.tileOccupation.set(currentTileID, true);
    
                    PathTransition pathTransitionPlayer = new PathTransition();
                    pathTransitionPlayer.setPath(path);
                    pathTransitionPlayer.setNode(player.getPlayerCharacter());
                    pathTransitionPlayer.setDuration(Duration.seconds(0.85));
                    pathTransitionPlayer.setCycleCount(1);
                    pathTransitionPlayer.setAutoReverse(false);
    
                    Path pathForOccupyingPlayer = new Path();
                    pathForOccupyingPlayer.getElements().addAll(
                        new MoveTo(this.tileCoordinates.get(currentTileID).getX(), this.tileCoordinates.get(currentTileID).getY()),
                        new LineTo(this.tileCoordinates.get(previousTileID).getX(), this.tileCoordinates.get(previousTileID).getY())
                    );
    
                    PathTransition pathTransitionOccupyingPlayer = new PathTransition();
                    pathTransitionOccupyingPlayer.setPath(pathForOccupyingPlayer);
                    pathTransitionOccupyingPlayer.setNode(occupyingPlayer.getPlayerCharacter());
                    pathTransitionOccupyingPlayer.setDuration(Duration.seconds(0.85));
                    pathTransitionOccupyingPlayer.setCycleCount(1);
                    pathTransitionOccupyingPlayer.setAutoReverse(false);
    
                    pathTransitionPlayer.setOnFinished((e) -> {
                        if (player.getTilesLeft() == 0) {
                            this.gameMode.handleWin();
                        }
                    });
    
                    this.nextTurn();
                    pathTransitionPlayer.play();
                    pathTransitionOccupyingPlayer.play();
                }
            } else {
                player.setCurrentTileID(currentTileID);
                player.setCurrentTileType(this.tiles.get(currentTileID));
                player.setTilesLeft(player.getTilesLeft() - cardValue);
    
                this.tileOccupation.set(previousTileID, false);
                this.tileOccupation.set(currentTileID, true);
    
                PathTransition pathTransition = new PathTransition();
                pathTransition.setPath(path);
                pathTransition.setNode(player.getPlayerCharacter());
                pathTransition.setDuration(Duration.seconds(0.85));
                pathTransition.setCycleCount(1);
                pathTransition.setAutoReverse(false);
                pathTransition.setOnFinished((e) -> {
                    if (player.getTilesLeft() == 0) {
                        this.gameMode.handleWin();
                    }
                });
    
                pathTransition.play();
            }
        } else {
            this.nextTurn();
        }
    }

    /**
     * execute the logic of handling the special swapping card when clicked
     *
     */
    @Override
    public void handleSwapCardAction() {
        PlayerController playerToSwap = null;

        int playerID = this.turnController.getTurnPlayerID();
        PlayerController player = this.players.get(playerID);
        int currentTileID = player.getCurrentTileID();
        boolean fromCave = false;
        boolean goesFoward = false;

        if (currentTileID == 0){
            fromCave = true;
            currentTileID = player.getCaveTileID();
        }

        int closestTileDifference = Integer.MAX_VALUE;

        for (PlayerController possiblePlayer : players) {
            if (! (possiblePlayer == player) && possiblePlayer.getCurrentTileID() != 0) {
                int possiblePlayerTileID = possiblePlayer.getCurrentTileID();
                // Calculate forward and backward to the target


                int forwardDistance = Math.floorMod( possiblePlayerTileID - currentTileID, this.tiles.size()-1); // check the step needed to reach the other player if go forward
                int backwardDistance = Math.floorMod(currentTileID - possiblePlayerTileID, this.tiles.size()-1); // check the step needed to reach the other player if go backward

                // Determine the shortest distance
                int tileDifference = Math.min(forwardDistance, backwardDistance); // use the minimum

                if (tileDifference < closestTileDifference) { // update if found a new minimum
                    if(forwardDistance <= backwardDistance){
                        goesFoward = true;
                    }
                    closestTileDifference = tileDifference;
                    playerToSwap = possiblePlayer;
                }
            }
        }

        if(playerToSwap != null)
        {
            if (goesFoward) { // goes forward
                playerToSwap.setTilesLeft(playerToSwap.getTilesLeft() + closestTileDifference);

                if (player.getTilesLeft() - Math.abs(closestTileDifference) < 0) { // if pass own cave after one loop
                    player.setTilesLeft(this.tiles.size() - Math.abs(closestTileDifference) + player.getTilesLeft() - 1);
                } else {
                    player.setTilesLeft(player.getTilesLeft() - closestTileDifference);
                }

            } else { // goes backward
                player.setTilesLeft(player.getTilesLeft() + closestTileDifference);
                if (playerToSwap.getTilesLeft() - Math.abs(closestTileDifference) < 0) { //if pass own cave after one loop
                    playerToSwap.setTilesLeft(this.tiles.size() - Math.abs(closestTileDifference) + player.getTilesLeft() - 1);
                } else {
                    playerToSwap.setTilesLeft(playerToSwap.getTilesLeft() - closestTileDifference);
                }
            }


            CardType previousCurrentType = player.getCurrentTileType();
            player.setCurrentTileType(playerToSwap.getCurrentTileType());
            playerToSwap.setCurrentTileType(previousCurrentType);

            int previousCurrentTileId = player.getCurrentTileID();
            player.setCurrentTileID(playerToSwap.getCurrentTileID());
            playerToSwap.setCurrentTileID(previousCurrentTileId);


            Path pathPlayer = new Path();
            Path pathSwapPlayer = new Path();

            if (fromCave) {

                playerToSwap.setCaveTileID(player.getCaveTileID()); // update the current cave TILE id to the swapped cave

                playerToSwap.setCurrentCaveID(player.getCurrentCaveID()); // update the cave ID


                pathPlayer.getElements().addAll(
                        new MoveTo(this.caveCoordinates.get(player.getCurrentCaveID()).getX(),this.caveCoordinates.get(player.getCurrentCaveID()).getY()),
                        new LineTo(this.tileCoordinates.get(player.getCurrentTileID()).getX(), this.tileCoordinates.get(player.getCurrentTileID()).getY())
                );

                pathSwapPlayer.getElements().addAll(
                        new MoveTo(this.tileCoordinates.get(player.getCurrentTileID()).getX(), this.tileCoordinates.get(player.getCurrentTileID()).getY()),
                        new LineTo(this.caveCoordinates.get(playerToSwap.getCurrentCaveID()).getX(), this.caveCoordinates.get(playerToSwap.getCurrentCaveID()).getY())

                );
            } else {

                pathPlayer.getElements().addAll(
                        new MoveTo(this.tileCoordinates.get(playerToSwap.getCurrentTileID()).getX(), this.tileCoordinates.get(playerToSwap.getCurrentTileID()).getY()),
                        new LineTo(this.tileCoordinates.get(player.getCurrentTileID()).getX(), this.tileCoordinates.get(player.getCurrentTileID()).getY())
                );
                pathSwapPlayer.getElements().addAll(
                        new MoveTo(this.tileCoordinates.get(player.getCurrentTileID()).getX(), this.tileCoordinates.get(player.getCurrentTileID()).getY()),
                        new LineTo(this.tileCoordinates.get(playerToSwap.getCurrentTileID()).getX(), this.tileCoordinates.get(playerToSwap.getCurrentTileID()).getY())
                );
            }


            PathTransition pathTransitionPlayer = new PathTransition();
            pathTransitionPlayer.setPath(pathPlayer);
            pathTransitionPlayer.setNode(player.getPlayerCharacter());
            pathTransitionPlayer.setDuration(Duration.seconds(0.85));
            pathTransitionPlayer.setCycleCount(1);
            pathTransitionPlayer.setAutoReverse(false);


            PathTransition pathTransitionOccupyingPlayer = new PathTransition();
            pathTransitionOccupyingPlayer.setPath(pathSwapPlayer);
            pathTransitionOccupyingPlayer.setNode(playerToSwap.getPlayerCharacter());
            pathTransitionOccupyingPlayer.setDuration(Duration.seconds(0.85));
            pathTransitionOccupyingPlayer.setCycleCount(1);
            pathTransitionOccupyingPlayer.setAutoReverse(false);

            pathTransitionPlayer.play();
            pathTransitionOccupyingPlayer.play();


        }
        this.nextTurn();
    }


    

    /**
     * Gets the string representation of the current player's turn.
     *
     * @return the string indicating the current player's turn
     */
    private String getTurnPlayerString() {
        return "PLAYER " + (this.turnController.getTurnPlayerID() + 1) + "'S TURN";
    }


    /**
     * Saves the GameBoard's state into the provided properties map.
     *
     * @param properties the map to populate with the GameBoard's state
     * @param prefix a prefix to prepend to property keys (used for nested objects)
     */
    @Override
    public void invokeSave(Map<String, String> properties, String prefix) {
        properties.put("playerCount", String.valueOf(this.players.size()));
        properties.put("currentPlayer", String.valueOf(this.turnController.getTurnPlayerID() + 1));
        properties.put("volcanoCardCount", String.valueOf(this.volcanoCards.size()));
        properties.put("cardCount", String.valueOf(this.dragonCards.size()));

        for(int i = 0; i < this.volcanoCards.size(); i++) {
            this.volcanoCards.get(i).invokeSave(properties, "volcanoCard" + (i + 1));
        }

        for(int i = 0; i < this.caves.size(); i++) {
            String key = "cave" + (i + 1);
            properties.put(key, this.caves.get(i).toString());
        }

        for(int i = 0; i < this.players.size(); i++) {
            this.players.get(i).invokeSave(properties, "player" + (i + 1));
        }

        for(int i = 0; i < this.dragonCards.size(); i++) {
            this.dragonCards.get(i).invokeSave(properties, "dragonCard" + (i + 1));
        }
    }
}
