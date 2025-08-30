export default class MenuScene extends Phaser.Scene {
    constructor() {
        super({ key: 'MenuScene' });
    }

    init(data) {
        this.winner = data?.winner || null;
    }

    create() {
        const { width, height } = this.scale;

        if (this.winner) {
            this.add.text(width / 2, height / 2 - 100, `${this.winner} Wins!`, { 
                fontSize: '40px',
                color: '#00ff00',
            }).setOrigin(0.5);
        }

        this.add.text(width / 2, height / 2 - 30, 'Duel Arena', { 
            fontSize: '48px',
            color: '#ffffff',
        }).setOrigin(0.5);

        this.add.text(width / 2, height / 2 + 30, 'Press SPACE to Start', { 
            fontSize: '24px',
            color: '#aaaaaa',
        }).setOrigin(0.5);

        this.input.keyboard.on('keydown-SPACE', () => {
            this.scene.start('GameScene');
        });
    }
}